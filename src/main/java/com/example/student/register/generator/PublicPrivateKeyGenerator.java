package com.example.student.register.generator;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class PublicPrivateKeyGenerator {
	
	String publicKey;
	String privateKey;
	
	public void generateKey() throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);

        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());

        privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
		
	}

}
