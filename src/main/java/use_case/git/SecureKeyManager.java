package use_case.git;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * A utility class for managing secure encryption keys using Java Keystore.
 * This class provides methods to save and load AES keys for encryption and decryption.
 */
public class SecureKeyManager {
    private static final String KEYSTORE_FILE = "keystore.jks";
    private static final String KEY_ALIAS = "githubEncryptionKey";
    private static final char[] KEYSTORE_PASSWORD = "changeit".toCharArray();

    /**
     * Generates a new AES key and stores it securely in a Java Keystore.
     *
     * @throws KeyException if an error occurs during key generation or storage.
     */
    public static void saveKey() throws KeyException {
        try {
            KeyStore keyStore = KeyStore.getInstance("JCEKS");
            keyStore.load(null, KEYSTORE_PASSWORD);

            // Generate a new AES key
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256);
            SecretKey secretKey = keyGen.generateKey();

            // Store the key in the KeyStore
            KeyStore.SecretKeyEntry keyEntry = new KeyStore.SecretKeyEntry(secretKey);
            KeyStore.ProtectionParameter protectionParam = new KeyStore.PasswordProtection(KEYSTORE_PASSWORD);
            keyStore.setEntry(KEY_ALIAS, keyEntry, protectionParam);

            // Save the KeyStore to a file
            try (FileOutputStream fos = new FileOutputStream(KEYSTORE_FILE)) {
                keyStore.store(fos, KEYSTORE_PASSWORD);
            }
        }
        catch (CertificateException | KeyStoreException | IOException | NoSuchAlgorithmException e) {
            throw new KeyException(e);
        }

    }

    /**
     * Loads the AES key from the Java Keystore.
     *
     * @return The AES key stored in the Keystore.
     * @throws KeyException if an error occurs during key retrieval.
     */
    public static SecretKey loadKey() throws KeyException {
        try (FileInputStream fis = new FileInputStream(KEYSTORE_FILE)) {
            KeyStore keyStore = KeyStore.getInstance("JCEKS");
            keyStore.load(fis, KEYSTORE_PASSWORD);
            KeyStore.ProtectionParameter protectionParam = new KeyStore.PasswordProtection(KEYSTORE_PASSWORD);
            KeyStore.SecretKeyEntry keyEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry(KEY_ALIAS, protectionParam);
            return keyEntry.getSecretKey();
        }
        catch (IOException | NoSuchAlgorithmException | CertificateException | UnrecoverableEntryException | KeyStoreException e) {
            throw new KeyException("File Not Found Exception", e);
        }
    }
}
