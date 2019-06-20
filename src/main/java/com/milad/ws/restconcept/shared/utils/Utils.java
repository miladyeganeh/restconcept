package com.milad.ws.restconcept.shared.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class Utils {

    private final String ALPHABET = "0987654321ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghilkmnopqrstuvwxyz";
    private final Random RANDOM = new SecureRandom();
    private final int ITERATION = 10000;
    private final int KEY_LENGTH = 256;

    public String generateUserId(int length){
        return generateRandomString(length);
    }

    private String generateRandomString(int length) {
        StringBuilder generatedValue = new StringBuilder();

        for (int i=0;i < length; i++){
            generatedValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return generatedValue.toString();
    }
}
