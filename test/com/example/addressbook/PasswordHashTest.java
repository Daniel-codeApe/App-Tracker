package com.example.addressbook;

import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class PasswordHashTest {

    @Test
    void encrypt() throws NoSuchAlgorithmException {
        String password1 = "123";
        String password2 = "123";
        String password3 = "456";

        assertEquals(PasswordHash.encrypt(password1), PasswordHash.encrypt(password2));
        assertNotEquals(PasswordHash.encrypt(password1), PasswordHash.encrypt(password3));
    }
}