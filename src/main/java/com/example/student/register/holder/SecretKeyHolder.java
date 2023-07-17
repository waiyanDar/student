package com.example.student.register.holder;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class SecretKeyHolder {

//    private String secretKey;
    
    public Map<String , String> userSecretKey= new HashMap<>();

//    public String getSecretKey() {
//        return secretKey;
//    }
//
//    public void setSecretKey(String secretKey) {
//        this.secretKey = secretKey;
//        System.out.println(userSecretKey.toString());
//    }
}
