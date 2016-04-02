package org.to2mbn.jmccc.exec;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;
import org.to2mbn.jmccc.util.Platform;

public class LoggingMonitor extends ProcessMonitor {

	private class LogMonitor implements Runnable {

		private Consumer<String> handler;
		private InputStream in;

		public LogMonitor(Consumer<String> handler, InputStream in) {
			this.handler = handler;
			this.in = in;
		}

		@Override
		public void run() {
			char[] eol = Platform.getLineSpearator().toCharArray();
			try {
				// no need for close this
				// because we don't need to close the base stream
				Reader reader = new InputStreamReader(in, Platform.getEncoding());

				StringBuilder buffer = new StringBuilder();
				int ch;
				while ((ch = reader.read()) != -1) {
					buffer.append((char) ch);

					if (buffer.length() >= eol.length) {
						// check eol
						boolean isEOL = true;
						for (int i = 0; i < eol.length; i++) {
							if (eol[i] != buffer.charAt(buffer.length() - eol.length + i)) {
								isEOL = false;
								break;
							}
						}

						if (isEOL) {
							buffer.delete(buffer.length() - eol.length, buffer.length());
							String log = buffer.toString();
							buffer.delete(0, buffer.length());
							handler.accept(log);
						}
					}

					if (Thread.interrupted()) {
						return;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	private class ExitMonitor implements Runnable {

		@Override
		public void run() {
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}

			int exitCode = process.exitValue();
			listener.onExit(exitCode);
		}

	}

	private GameProcessListener listener;

	public LoggingMonitor(Process process, GameProcessListener listener) {
		super(process);
		this.listener = listener;
	}

	public GameProcessListener getListener() {
		return listener;
	}

	@Override
	protected Collection<? extends Runnable> createMonitors() {
		return Arrays.asList(
				new LogMonitor(listener::onLog, process.getInputStream()),
				new LogMonitor(listener::onErrorLog, process.getErrorStream()),
				new ExitMonitor());
	}

}
