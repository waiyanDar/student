package com.example.student.register.holder;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class Holder {


    public Map<String , String> userSecretKey= new HashMap<>();

//    public String privateKey;
    
    public Map<String , LocalTime> otpHolder = new HashMap<>();

}
