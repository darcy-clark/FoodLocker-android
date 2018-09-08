package org.foodlocker.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashingUtil {

    public static String sha256(String unhashed) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        md.update(unhashed.getBytes());
        byte[] bytes = md.digest();
        return String.format( "%064x", new BigInteger( 1, bytes ) );
    }
}
