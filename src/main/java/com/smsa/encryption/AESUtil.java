/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smsa.encryption;

/**
 *
 * @author abcom
 */
import java.nio.charset.StandardCharsets;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public final class AESUtil {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    

    private AESUtil() {
        throw new UnsupportedOperationException("Utility class");
    }


    public static String encrypt(String data,String encryptionKey,String iv) {
        try {
            // 16-byte key for AES-128
            SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes(StandardCharsets.UTF_8), "AES");

            // Use the same fixed IV as decryption (NOT secure for real systems)
            byte[] ivBytes = iv.getBytes(StandardCharsets.UTF_8);  // 16 bytes
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

            byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

            // Encode result to Base64
            return Base64.getEncoder().encodeToString(encryptedBytes);

        } catch (Exception e) {
            return "";
        }
    }

    public static  String decrypt(String encryptedAcc,String encryptionKey,String iv) {
        try {
            // 16-byte key for AES-128
            SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes(StandardCharsets.UTF_8), "AES");

            // Example: fixed IV (not secure for production use)
            byte[] ivBytes = iv.getBytes(StandardCharsets.UTF_8);  // 16 bytes
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);

            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedAcc));
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "";
        }
    }
}
