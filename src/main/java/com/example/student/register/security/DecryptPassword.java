package com.example.student.register.security;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@Component
public class DecryptPassword {

    public String decryptPassword(String encryptedPsw, String generatePK){

        try{
//            System.out.println("private key : " + generatePK);

            byte [] privateKeyByte = Base64.getDecoder().decode(generatePK);
            PKCS8EncodedKeySpec keySpec =new PKCS8EncodedKeySpec(privateKeyByte);

            KeyFactory keyFactory =KeyFactory.getInstance("RSA");
            PrivateKey privateKey =keyFactory.generatePrivate(keySpec);

            Cipher cipher =Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte [] decryptedPswByte =cipher.doFinal(Base64.getDecoder().decode(encryptedPsw));

            String decryptedPsw =new String(decryptedPswByte, StandardCharsets.UTF_8);
            return decryptedPsw;
        }catch (Exception e){
            System.out.println(e.getCause());
            return "fail";
        }

    }
}
