package com.utar.uhauction.utils;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MD5Utils {

    public static String getPwd(String pwd) {
        try {
            //  create encrypt object
            MessageDigest digest = MessageDigest.getInstance("md5");

            // call encrypt object's method, encryption completed
            byte[] bs = digest.digest(pwd.getBytes());
            // optimize the result of encryption
            // mysql optimization:
            // step one, convert all data into positive:
            String hexString = "";
            for (byte b : bs) {

                // using b&255
                /*
                 * b:byte data type (one byte) 255: for int data type (four byte)
                 * compute with byte type data and int type data, automatically upgrade to int type eg: b: 1001 1100(original data)
                 * when compute: b: 0000 0000 0000 0000 0000 0000 1001 1100 255: 0000
                 * 0000 0000 0000 0000 0000 1111 1111 result: 0000 0000 0000 0000
                 * 0000 0000 1001 1100 now data type of temp is int
                 */
                int temp = b & 255;
                // step two, convert all the data into hexadecimal
                // caution: need to make sure the number>=0&&<16,
                // Integer.toHexString()，might be lead to lack of digit
                // check for temp
                if (temp < 16 && temp >= 0) {
                    // add a "0" manually
                    hexString = hexString + "0" + Integer.toHexString(temp);
                } else {
                    hexString = hexString + Integer.toHexString(temp);
                }
            }
            return hexString;
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }



}
