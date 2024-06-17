package com.example.addressbook;
import java.math.BigInteger;
import java.security.*;

public class PasswordHash {
    public static String encrypt(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(password.getBytes());
        BigInteger bigInteger = new BigInteger(1, messageDigest);
        return bigInteger.toString(16);
    }
}
