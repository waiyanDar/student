package com.example.student.register.holder;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
public class SecretKeyHolder {

    private String secretKey;

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
