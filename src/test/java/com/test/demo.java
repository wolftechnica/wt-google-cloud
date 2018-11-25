package com.test;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

import com.wolftechnica.google.exceptions.WtCloudException;
import com.wolftechnica.google.helpers.WtHelper;
import com.wolftechnica.google.service.GoogleClouldStorageService;

public class demo {

	static String apiKey = "E:\\xtra\\_bkp\\tememp\\src\\main\\resources\\springboot-tempemp3-72e44c75e603.json";
	static String projectId = "springboot-tempemp3";
	static String bucketName = "tempempbucket";
	
	static GoogleClouldStorageService clouldStorageService;
	
	 static {
		try {
			clouldStorageService = new GoogleClouldStorageService(apiKey, projectId, bucketName);
		}  catch (WtCloudException e) {
			e.printStackTrace();
		}
	}
	 
	public static void main(String[] args) throws WtCloudException, IOException {
		//GCSCoreService coreService = GCSCoreService.getGCSCoreService(apiKey, projectId)
/*		File f = new File("C:\\Users\\a1r1q88i\\Desktop\\static\\certificateBG.jpg");
		byte[] fileContent = Files.readAllBytes(f.toPath());
*/		/*//new FileInputStream(f).
		byte[] fileContent = Files.readAllBytes(f.toPath());

		
		WtBlobId uploadFileInFolder = clouldStorageService.uploadFileInFolder(fileContent, FileExtenion.IMG_PNG,"profile/");
		System.out.println(uploadFileInFolder.getBucket());
		System.out.println(uploadFileInFolder.getGeneration());
		System.out.println(uploadFileInFolder.getName());
		
		
		WtBlob fileFromBucket = clouldStorageService.getFileFromBucket(uploadFileInFolder);
		
		System.out.println(fileFromBucket.getMediaLink());*/

		
		File f = new File("C:\\Users\\a1r1q88i\\Desktop\\static\\cr_841x595.jpg");
		byte[] fileContent = Files.readAllBytes(f.toPath());
		byte[] res = WtHelper.resizeImage(fileContent, 450, 450, "jpeg");
		try (FileOutputStream fos = new FileOutputStream("C:\\Users\\a1r1q88i\\Desktop\\static\\cr_450x450.jpg")) {
			   fos.write(res);
		}
		
		f = new File("C:\\Users\\a1r1q88i\\Desktop\\static\\cr_841x595.jpg");
		fileContent = Files.readAllBytes(f.toPath());
		res = WtHelper.resizeImage(fileContent, 150, 150, "jpeg");
		try (FileOutputStream fos = new FileOutputStream("C:\\Users\\a1r1q88i\\Desktop\\static\\cr_150x150.jpg")) {
			   fos.write(res);
		}
		
		
		
	}
}
