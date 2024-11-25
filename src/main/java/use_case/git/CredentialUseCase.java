package use_case.git;

import entity.CredentialEncryption;

import javax.crypto.SecretKey;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CredentialUseCase {
    private static final String CREDENTIALS_FILE = "credentials.dat";
    private static final String SALT_FILE = "salt.dat";

    public void saveCredentials(String username, String password) throws Exception {
        // Generate or load salt in a more explicit way
        byte[] salt = getSalt();
        SecretKey key = CredentialEncryption.deriveKey(password.toCharArray(), salt);

        String credentials = username + ":" + password;
        byte[] encryptedData = CredentialEncryption.encrypt(credentials, key);
        saveToFile(CREDENTIALS_FILE, encryptedData);
    }

    public String[] loadCredentials(String password) throws Exception {
        // Get salt and derive the key
        byte[] salt = getSalt();
        SecretKey key = CredentialEncryption.deriveKey(password.toCharArray(), salt);

        byte[] encryptedData = readFromFile(CREDENTIALS_FILE);
        String decryptedData = CredentialEncryption.decrypt(encryptedData, key);
        return decryptedData.split(":");
    }

    private byte[] getSalt() throws IOException {
        // Separate the logic of checking or generating salt
        File saltFile = new File(SALT_FILE);
        if (!saltFile.exists()) {
            return generateAndSaveSalt();
        }
        return readSaltFromFile();
    }

    private byte[] generateAndSaveSalt() throws IOException {
        byte[] salt = CredentialEncryption.generateSalt();
        saveToFile(SALT_FILE, salt);
        return salt;
    }

    private byte[] readSaltFromFile() throws IOException {
        if (!Files.exists(Paths.get(SALT_FILE))) {
            throw new FileNotFoundException("Salt file not found. Ensure credentials have been saved first.");
        }
        return Files.readAllBytes(Paths.get(SALT_FILE));
    }

    private void saveToFile(String fileName, byte[] data) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(data);
        }
    }

    private byte[] readFromFile(String fileName) throws IOException {
        File file = new File(fileName);
        byte[] data = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(data);
        }
        return data;
    }
}
