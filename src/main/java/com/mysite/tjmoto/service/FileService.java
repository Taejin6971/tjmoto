package com.mysite.tjmoto.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.extern.java.Log;

@Service
@Log
public class FileService {

	public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception {

		UUID uuid = UUID.randomUUID();
		// 원본 파일의 확장자만 저장
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
		// 서버에 저장할 파일이름
		String savedFileName = uuid.toString() + extension;
		// c:/tjmoto/item/UUID.jpg
		String fileUploadFullUrl = uploadPath + "/" + savedFileName;

		FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
		fos.write(fileData);
		fos.close();
		return savedFileName;
	}

	public void deleteFile(String filePath) throws Exception {
		File deleteFile = new File(filePath);
		if (deleteFile.exists()) {
			deleteFile.delete();
			log.info("파일을 삭제하였습니다.");
		} else {
			log.info("파일이 존재하지 않습니다.");
		}
	}

}