package com.wolftechnica.google.dto;

/**
 * WtBlob : getter/setter implementation of Blob object consist of blobId, medialink and content in for of array of bytes.
 * 
 * @author Wolftechnica services
 *
 */
public class WtBlob {

	private WtBlobId blobId;

	private String mediaLink;

	private byte[] content;

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public WtBlobId getBlobId() {
		return blobId;
	}

	public void setBlobId(WtBlobId blobId) {
		this.blobId = blobId;
	}

	public String getMediaLink() {
		return mediaLink;
	}

	public void setMediaLink(String mediaLink) {
		this.mediaLink = mediaLink;
	}

	@Override
	public String toString() {
		return "BlobDTO [blobId=" + blobId + ", mediaLink=" + mediaLink + "]";
	}

}
