package com.wolftechnica.google.helpers;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.wolftechnica.google.dto.WtBlob;
import com.wolftechnica.google.dto.WtBlobId;

public class DtoMapper {

	public static WtBlob objectToDto(Blob blob) {
		if (blob == null) {
			return null;
		}
		WtBlob blobDTO = new WtBlob();
		WtBlobId blobIdDto = new WtBlobId();
		BlobId blo = blob.getBlobId();
		if (blo != null) {
			blobIdDto.setBucket(blo.getBucket());
			blobIdDto.setGeneration(blo.getGeneration());
			blobIdDto.setName(blo.getName());
		}
		blobDTO.setBlobId(blobIdDto);
		blobDTO.setMediaLink(blob.getMediaLink());
		blobDTO.setContent(blob.getContent());
		return blobDTO;
	}

	private DtoMapper() throws IllegalAccessException {
		throw new IllegalAccessException();
	}

}
