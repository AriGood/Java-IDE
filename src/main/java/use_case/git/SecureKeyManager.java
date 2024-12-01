package use_case.git;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.security.KeyException;
import java.security.KeyStore;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * A utility class for managing secure encryption keys using Java Keystore.
 * This class provides methods to save and load AES keys for encryption and decryption.
 */
public class SecureKeyManager {
    private static final String KEYSTORE_FILE = "keystore.jks"; // Keystore file name
    private static final String KEY_ALIAS = "githubEncryptionKey"; // Alias for the key entry
    private static final char[] KEYSTORE_PASSWORD = "changeit".toCharArray(); // Keystore password

    /**
     * Generates a new AES key and stores it securely in a Java Keystore.
     *
     * @throws Exception if an error occurs during key generation or storage.
     */
    public static void saveKey() throws Exception {
        // Create a KeyStore instance
        KeyStore keyStore = KeyStore.getInstance("JCEKS");
        keyStore.load(null, KEYSTORE_PASSWORD);

        // Generate a new AES key
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256); // AES-256 for strong encryption
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

    /**
     * Loads the AES key from the Java Keystore.
     *
     * @return The AES key stored in the Keystore.
     * @throws Exception if an error occurs during key retrieval.
     */
    public static SecretKey loadKey() throws KeyException {
        try {
            // Load the KeyStore from the file
            KeyStore keyStore = KeyStore.getInstance("JCEKS");
            try (FileInputStream fis = new FileInputStream(KEYSTORE_FILE)) {
                keyStore.load(fis, KEYSTORE_PASSWORD);
            }

            // Retrieve the key using its alias and password protection
            KeyStore.ProtectionParameter protectionParam = new KeyStore.PasswordProtection(KEYSTORE_PASSWORD);
            KeyStore.SecretKeyEntry keyEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry(KEY_ALIAS, protectionParam);
            return keyEntry.getSecretKey();

        } catch (Exception e) {
            throw new KeyException("Error loading key", e);
        }
    }
}
