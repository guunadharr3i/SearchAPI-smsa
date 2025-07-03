/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smsa.encryption;

/**
 *
 * @author abcom
 */
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.security.GeneralSecurityException;
import java.util.Base64;

public final class AESUtil {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    

    private AESUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String encrypt(String plainText,String secretkey) throws GeneralSecurityException {
        // Generate random 16-byte IV
        byte[] ivBytes = new byte[16];
        new SecureRandom().nextBytes(ivBytes);
        IvParameterSpec iv = new IvParameterSpec(ivBytes);

        SecretKeySpec skeySpec = new SecretKeySpec(secretkey.getBytes(), "AES");

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

        byte[] encrypted = cipher.doFinal(plainText.getBytes());

        // Prepend IV to ciphertext (IV + encrypted)
        byte[] combined = new byte[ivBytes.length + encrypted.length];
        System.arraycopy(ivBytes, 0, combined, 0, ivBytes.length);
        System.arraycopy(encrypted, 0, combined, ivBytes.length, encrypted.length);

        return Base64.getEncoder().encodeToString(combined);
    }

    public static String decrypt(String encryptedBase64,String secretkey) throws GeneralSecurityException {
        byte[] combined = Base64.getDecoder().decode(encryptedBase64);

        // Extract IV and encrypted text
        byte[] ivBytes = new byte[16];
        byte[] encryptedBytes = new byte[combined.length - 16];

        System.arraycopy(combined, 0, ivBytes, 0, 16);
        System.arraycopy(combined, 16, encryptedBytes, 0, encryptedBytes.length);

        IvParameterSpec iv = new IvParameterSpec(ivBytes);
        SecretKeySpec skeySpec = new SecretKeySpec(secretkey.getBytes(), "AES");

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

        byte[] original = cipher.doFinal(encryptedBytes);
        return new String(original);
    }
}
