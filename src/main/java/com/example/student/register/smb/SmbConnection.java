package com.example.student.register.smb;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.student.register.config.ConfigForSmb;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.SmbConfig;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class SmbConnection {
	
	@Autowired
	private ConfigForSmb configForSmb;
	
	private final Logger debugLogger = LoggerFactory.getLogger("debug ");
	private final Logger errorLogger = LoggerFactory.getLogger("error ");
	
	private DiskShare diskShare;
	private Session session;
	private SMBClient smbClient;
	private Connection connection;
	
	public boolean checkConnection() {
		return diskShare != null && diskShare.isConnected();
	}
	
	public void getConnection () {
		diskShare =(DiskShare) createSession().connectShare(configForSmb.SMB_SHARE_NAME);
		
		if (diskShare != null) {
			debugLogger.info("Successfully connected to " + configForSmb.SMB_SERVER_NAME);
		}else {
			errorLogger.error("Fail to connect to " + configForSmb.SMB_SERVER_NAME);
		}
	}
	
	private Session createSession() {
		session = createConnection().authenticate(createAuthContext());
		return session;
	}
	
	private AuthenticationContext createAuthContext() {
		char [] charPassword = configForSmb.SMB_PASSWORD.toCharArray();
		return new AuthenticationContext(configForSmb.SMB_USERNAME, charPassword, configForSmb.SMB_DOMAIN);
	}
	
	private Connection createConnection() {
		try {
			
			connection = createClient().connect(configForSmb.SMB_SERVER_NAME);
			return connection;
		} catch (IOException e) {
			errorLogger.error("Failed to connect to " + configForSmb.SMB_SERVER_NAME);
			return null;
		}
		
	}
	
	private SMBClient createClient() {
		smbClient = new SMBClient(getConfig());
		return smbClient;
	}

	private SmbConfig getConfig() {
		return SmbConfig.builder().build();
	}
	
	public void closeConnection() {
		try {
			if (diskShare != null) {
				diskShare.close();
				diskShare = null;
			}
			
			if (session != null) {
				session.close();
				session = null;
			}
			
			if (connection != null) {
				connection.close();
				connection = null;
			}
			
			if (smbClient != null) {
				smbClient.close();
				smbClient = null;
			}
			
			debugLogger.info("Connection is successfully closed");
			
		} catch (Exception e) {
			
			errorLogger.error("Connection is failed to closed");
		}
	}
}
