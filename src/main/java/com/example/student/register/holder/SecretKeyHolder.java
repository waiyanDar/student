package com.example.student.register.holder;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class SecretKeyHolder {


    public Map<String , String> userSecretKey= new HashMap<>();

    public String privateKey;

}
