package entity;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Utility class for secure encryption and decryption of credentials.
 * Provides methods for generating salt, deriving keys, and performing AES encryption/decryption.
 */
public class CredentialEncryption {

    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 128;

    /**
     * Generates a random 128-bit salt for key derivation.
     *
     * @return a byte array representing the generated salt.
     */
    public static byte[] generateSalt() {
        byte[] salt = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return salt;
    }

    /**
     * Derives a cryptographic key from a password and a salt using PBKDF2 with HMAC-SHA256.
     *
     * @param password the password to derive the key from.
     * @param salt     the salt to use in the key derivation.
     * @return a SecretKey for AES encryption/decryption.
     * @throws EncryptionException if the key derivation process fails.
     */
    public static SecretKey deriveKey(char[] password, byte[] salt) throws EncryptionException {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] keyBytes = factory.generateSecret(spec).getEncoded();
            return new SecretKeySpec(keyBytes, "AES");
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new EncryptionException(e);
        }
    }

    /**
     * Encrypts a given string using the specified AES key.
     *
     * @param data the plaintext string to encrypt.
     * @param key  the AES key to use for encryption.
     * @return a byte array containing the encrypted data.
     * @throws EncryptionException if the encryption process fails.
     */
    public static byte[] encrypt(String data, SecretKey key) throws EncryptionException {
        try {
            javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES");
            cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(data.getBytes());
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | InvalidKeyException |
                 BadPaddingException e) {
            throw new EncryptionException(e);
        }
    }

    /**
     * Decrypts a given byte array using the specified AES key.
     *
     * @param data the encrypted byte array to decrypt.
     * @param key  the AES key to use for decryption.
     * @return the decrypted plaintext string.
     * @throws EncryptionException if the decryption process fails.
     */
    public static String decrypt(byte[] data, SecretKey key) throws EncryptionException {
        try{
            javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES");
            cipher.init(javax.crypto.Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(data));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | InvalidKeyException | BadPaddingException e) {
            throw new EncryptionException(e);
        }
    }

    /**
     * Encodes a byte array as a Base64-encoded string.
     *
     * @param data the byte array to encode.
     * @return a Base64-encoded string representation of the byte array.
     */
    public static String encodeBase64(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    /**
     * Decodes a Base64-encoded string into its original byte array form.
     *
     * @param data the Base64-encoded string to decode.
     * @return a byte array representing the decoded data.
     */
    public static byte[] decodeBase64(String data) {
        return Base64.getDecoder().decode(data);
    }

    public static class EncryptionException extends Exception{
        EncryptionException(Exception ex) {
            super(ex);
        }
    }
}
