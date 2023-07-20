package com.example.student.register.security;

import org.springframework.stereotype.Component;

//import com.example.student.register.generator.PublicPrivateKeyGenerator;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@Component
public class DecryptPassword {

//	@Autowired
//	private PublicPrivateKeyGenerator generator;
//	
	private static final String PRIVATE_KEY="MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC/OF9I8wxMh1dkPyfjXIAAVwCAzXEMFDTMcNH9OJ7C6pQRzrAUHpgs1nzg2aJX15T+LQMXJeh+r9tDTpwr5q2H6Kl/65o0UZQeL0GoZlFFF65pAgZkme0S4nH2cBX4TTZMnB6ZyEdGWj+SJfBieNu6Vw2dI8sO5tJ9LTrbwg/jaXXUqomilFRcdQIJBkPBlef+xuPw1UP6GFsUT9b9Fec9VUeMxwWIlS9swNA7LZOB5B87GIaaI0tyobhSr3P+0xNocPu8xSq1MhwI7/nuRwyv3tUQZh/qQtLXZUFPDVPBVDaluohowfJiTQEfV6aZLluv7NfREGgVh/IZyBbxhMT3AgMBAAECggEAL0X2eIhtv/Wtq0DZ0aGqm2rm2vDvQ7czHl+96kY3KaTomkZozXCK2nrkQIpNUj4fvX7PRse+ZKOBJ3Clt0y+Fd11GgTbCJgqcBPqvYEeEmC6Y4d2oSRUsdh9qBkptUsRY3stLYOP61qgYLc0aIPMfpLK8NBKgRKbcwrdhDrDxsp8Ld0g/vyxScScOHgvWECDXJsQjiULpo36Lq4DTYwdhNX5nc75KQCeszzRE5AdsWuUCdrmUeSGEgdGJfo5HR8j0O+C4ogcIjVdhFRrDcRGy1kgV28l7BMKQ+eDujgooXbv+Lt9PECwXWd/R2NGMBLCiY4uy68/zZVCcMOq63Wj0QKBgQDnoDNhfhL3IvbUkrfAzRMTuqkvOVx2ej7/RHKzIUqkzYCxPgbaKj5aeL9QUWdqVYSFiThviq6bDfokq5deFMjaL7s32W1DDRCrdcGTVJkWHch17cntp9cmohHpazOzZ+Ysoo+KEO9xZ1PYqWrCBZTwBkxzKIEFN+h5SV5cR1JKewKBgQDTV64Q2c4ukv0WaG1rmFVEPhLRG157eC0Gy3fb6PL/LL/ZhsUdjyqQQTb+yQxTrx5SOORKTgM11exOZSMIrwlrLnteivieBXTW1zgkxb3xxfIpZ5JY0zOLO8aRB2cvggtDX5wH7Bh1ccucM4XZPfokRVhWWGM3Tmv+U/990KWUtQKBgQCwQgE3bCfkdm/cPTWleqjMt5ts+zp4UfhEagLaezgBBvyqY6f1NnYyhHYZGYkXqrHoqA4RGzhZd55lb0oFZs2c8vuFuWfiwTtxLm+6vIKuCMX1r5icx776gBQfStuR5zuJtb6C8vYyls2ALxO1R2gZ6sEcVLMxlGTMft9WW1OspwKBgQC7qA8XR06cbEftrav67bhIcBWlNvgeCgdxERnMWnvpuIJhVBmStZzFmiKK+VnItKXnvDx4/ew70UeSFJuXClUCprNEr4EyTwX//foMLMkL9QehXCXGWUJbTyhxfnFQ7deyayLQpgLRxclVWSpwS2fg7PITOUhfw3KA0XT68lkUQQKBgD1f/zwwsC96dj4Qy0T7iqgL+wgvpFgoRyiV9+Z06MzUF/IRjRNeozaVrpR0zeB2Rn8iyb9bsWqVEiiCFhxaOgxuSh1AedyTtL/tu0ronSILwfvjEDGf2lNWe/56KNX5v1JoDcn/H+pq5Y5eh4rfd7tnwzQaJt0JdrjRbj+IIHCX";
	
    public String decryptPassword(String encryptedPsw){

        try{
//            System.out.println("private key : " + generatePK);
//            System.out.println("public key  : " + generator.getPublicKey());

            byte [] privateKeyByte = Base64.getDecoder().decode(PRIVATE_KEY);
            PKCS8EncodedKeySpec keySpec =new PKCS8EncodedKeySpec(privateKeyByte);

            KeyFactory keyFactory =KeyFactory.getInstance("RSA");
            PrivateKey privateKey =keyFactory.generatePrivate(keySpec);

            Cipher cipher =Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte [] decryptedPswByte =cipher.doFinal(Base64.getDecoder().decode(encryptedPsw));

            String decryptedPsw =new String(decryptedPswByte, StandardCharsets.UTF_8);
            return decryptedPsw;
        }catch (Exception e){
//            System.out.println(e.getCause());
        	e.printStackTrace();
            return "fail";
        }

    }
}
