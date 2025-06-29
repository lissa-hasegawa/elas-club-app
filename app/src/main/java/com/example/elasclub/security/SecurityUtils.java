package com.example.elasclub.security;

import org.mindrot.jbcrypt.BCrypt;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class SecurityUtils {

    // Hash com BCrypt
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean verifyPassword(String password, String hashed) {
        return BCrypt.checkpw(password, hashed);
    }
}
