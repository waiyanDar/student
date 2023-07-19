package com.example.student.register.generator;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import com.example.student.register.holder.SecretKeyHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class PublicPrivateKeyGenerator {
	
	String publicKey;
	String privateKey;

	@Autowired
	private SecretKeyHolder holder;

	public void generateKey() throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);

        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());

        privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());

		holder.privateKey= privateKey;
//		System.out.println("private key from generator : "+privateKey);
//		System.out.println("public key : "+publicKey);
	}

}
