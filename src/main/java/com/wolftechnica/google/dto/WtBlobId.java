package com.wolftechnica.google.dto;

/**
 * WtBlobId : getter/setter implementation of BlobID object consist of bucket, name of file and generation of stored file.
 * 
 * @author Wolftechnica services
 *
 */
public class WtBlobId {
	
	private String bucket;
	
	private String name;
	
	private long generation;

	private String mediaLink;
	
	private String mediaLinkThumbailed;
	
	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getGeneration() {
		return generation;
	}

	public void setGeneration(long generation) {
		this.generation = generation;
	}
	
	public String getMediaLink() {
		return mediaLink;
	}

	public void setMediaLink(String mediaLink) {
		this.mediaLink = mediaLink;
	}

	public String getMediaLinkThumbailed() {
		return mediaLinkThumbailed;
	}

	public void setMediaLinkThumbailed(String mediaLinkThumbailed) {
		this.mediaLinkThumbailed = mediaLinkThumbailed;
	}

	@Override
	public String toString() {
		return "WtBlobId [bucket=" + bucket + ", name=" + name + ", generation=" + generation + ", mediaLink="
				+ mediaLink + ", mediaLinkThumbailed=" + mediaLinkThumbailed + "]";
	}

	
	
	

}
