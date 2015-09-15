package com.github.to2mbn.jmccc.mcdownloader;

public class Asset {

	private String virtualPath;
	private String hash;
	private int size;

	/**
	 * Creates an asset.
	 * 
	 * @param virtualPath the virtual path
	 * @param hash the sha1 hash
	 * @param size the size
	 */
	public Asset(String virtualPath, String hash, int size) {
		this.virtualPath = virtualPath;
		this.hash = hash;
		this.size = size;
	}

	/**
	 * Gets the virtual path.
	 * 
	 * @return the virtual path
	 */
	public String getVirtualPath() {
		return virtualPath;
	}

	/**
	 * Gets the sha1 hash of the asset.
	 * 
	 * @return the sha1 hash of the asset
	 */
	public String getHash() {
		return hash;
	}

	/**
	 * Gets the size of the asset.
	 * 
	 * @return the size of the asset
	 */
	public int getSize() {
		return size;
	}

	@Override
	public String toString() {
		return "[path=" + virtualPath + ", hash=" + hash + ", size=" + size + "]";
	}

	/**
	 * Returns the abstract path of the asset in hash directory structure.
	 * <p>
	 * The path is <code>hash.substring(0, 2) + "/" + hash</code>.
	 * 
	 * @return the abstract path of the asset in hash directory structure
	 */
	public String getHashPath() {
		return hash.substring(0, 2) + "/" + hash;
	}

}
