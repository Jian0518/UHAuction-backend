package com.utar.uhauction.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256Utils {

    public static String getPwd(String pwd) {
        try {
            // Create the SHA-256 digest object
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Call the digest object's method to perform the hashing
            byte[] hashBytes = digest.digest(pwd.getBytes());

            // Convert the hashed bytes to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // Handle the exception
            e.printStackTrace();
        }
        return "";
    }
}
