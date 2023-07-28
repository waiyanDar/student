package com.example.student.register.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.student.register.config.ConfigForSmb;
import com.example.student.register.smb.SmbConnection;
import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.hierynomus.smbj.io.InputStreamByteChunkProvider;
import com.hierynomus.smbj.share.DiskShare;
import com.hierynomus.smbj.share.File;

import lombok.Data;

@Service
@Data
public class SmbService {

	@Autowired
	private SmbConnection smbConnection;

	@Autowired
	private ConfigForSmb configForSmb;

	private static Logger debugLogger = LoggerFactory.getLogger("debug ");
	private static Logger errorLogger = LoggerFactory.getLogger("error ");

	public String storePhoto(MultipartFile photo) {
		try {

			if (!smbConnection.checkConnection()) {

				smbConnection.getConnection();
				System.out.println("check connection");
			}

			String path = writePhoto(photo);

			debugLogger.info("Successfully uploaded " + photo.getOriginalFilename());
			return path;
			
		} catch (Exception e) {
			errorLogger.error("Fial to upload " + photo.getOriginalFilename());
			return null;
		}
	}

	private String writePhoto(MultipartFile photo) throws IOException {

		DiskShare diskShare = smbConnection.getDiskShare();

		String folderPath = configForSmb.SMB_SHARE_NAME + "/" + configForSmb.FOLDER_PATH + "/"
				+ configForSmb.PHOTO_FOLDER_NAME;

		String directory = checkFolder(folderPath, diskShare);
		String fileName = directory + photo.getOriginalFilename();

		File file = diskShare.openFile(fileName, EnumSet.of(AccessMask.GENERIC_WRITE), null, SMB2ShareAccess.ALL,
				SMB2CreateDisposition.FILE_CREATE, null);
		
		InputStream fileInputStream = photo.getInputStream();

		file.write(new InputStreamByteChunkProvider(fileInputStream));

		return fileName;
	}

	private String checkFolder(String folderPath, DiskShare diskShare) {

		if (diskShare.fileExists(folderPath)) {
			debugLogger.info("Folder path is exists");
			return folderPath;
		} else {
			debugLogger.info("Folder path is created");
			return buildDirectory(folderPath, diskShare);
		}

	}

	private String buildDirectory(String folderPath, DiskShare diskShare) {
		
		String directory = "";

		List<String> folderList = Arrays.asList(folderPath.split("/"));
		
		for (String folderName : folderList) {
			
			if (!folderName.isEmpty()) {
				directory += folderName + "/";
				
				if (!diskShare.folderExists(directory)) {
					diskShare.mkdir(directory);
				}
			}
		}
		return directory;
	}
}
