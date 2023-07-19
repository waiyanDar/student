package com.example.student.register.security;

import com.example.student.register.generator.PublicPrivateKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@Component
public class DecryptPassword {

    @Autowired
    private PublicPrivateKeyGenerator generator;
    public String decryptPassword(String encryptedPsw, String generatePK){

        try{
            System.out.println("private key : " + generatePK);
            System.out.println("public k: " + generator.getPublicKey());
            byte [] privateKeyByte = Base64.getDecoder().decode(generatePK);
            PKCS8EncodedKeySpec keySpec =new PKCS8EncodedKeySpec(privateKeyByte);

            KeyFactory keyFactory =KeyFactory.getInstance("RSA");
            PrivateKey privateKey =keyFactory.generatePrivate(keySpec);

            Cipher cipher =Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte [] decryptedPswByte =cipher.doFinal(Base64.getDecoder().decode(encryptedPsw));

//            int paddingLength = decryptedPswByte[decryptedPswByte.length - 1];
//            byte [] decryptedPswNoPadding = new byte[decryptedPswByte.length - paddingLength];
//
//            String decryptedPsw =new String(decryptedPswNoPadding, StandardCharsets.UTF_8);
            String decryptedPsw =new String(decryptedPswByte, StandardCharsets.UTF_8);
            return decryptedPsw;
        }catch (Exception e){
            e.printStackTrace();
            return "fail";
        }

    }
}
