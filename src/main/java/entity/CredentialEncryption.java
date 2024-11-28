package entity;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import java.security.SecureRandom;
import java.util.Base64;

public class CredentialEncryption {

    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 128; // Bits

    // Generate a salt for key derivation
    public static byte[] generateSalt() {
        byte[] salt = new byte[16]; // 128-bit salt
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return salt;
    }

    // Derive a key from a password and salt
    public static SecretKey deriveKey(char[] password, byte[] salt) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] keyBytes = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(keyBytes, "AES");
    }

    // Encrypt a message using the derived key
    public static byte[] encrypt(String data, SecretKey key) throws Exception {
        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES");
        cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data.getBytes());
    }

    // Decrypt a message using the derived key
    public static String decrypt(byte[] data, SecretKey key) throws Exception {
        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES");
        cipher.init(javax.crypto.Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(data));
    }

    // Example to encode as Base64 (for storage or output)
    public static String encodeBase64(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    // Example to decode Base64 (to retrieve stored encrypted data)
    public static byte[] decodeBase64(String data) {
        return Base64.getDecoder().decode(data);
    }

}
