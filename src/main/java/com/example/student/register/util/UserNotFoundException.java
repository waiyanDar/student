package com.example.student.register.util;

import java.util.NoSuchElementException;

public class UserNotFoundException extends NoSuchElementException {
    public UserNotFoundException(String message){
        super(message);
    }
}
