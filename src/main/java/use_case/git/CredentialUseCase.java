package use_case.git;

import java.io.*;
import java.security.GeneralSecurityException;

import javax.crypto.SecretKey;
import javax.security.auth.login.LoginException;

import entity.CredentialEncryption;
import view.GitMenuBuilder;

public class CredentialUseCase {
    private static final String CREDENTIALS_FILE = "credentials.dat";

    /**
     * Save encrypted credentials to a file.
     *
     * @param username The username to save.
     * @param password The password to save.
     * @param secretKey The SecretKey used for encryption.
     * @throws GeneralSecurityException If an error occurs during encryption or saving.
     */
    public void saveCredentials(String username, String password, SecretKey secretKey) throws GeneralSecurityException {
        try {
            String credentials = username + ":" + password;
            byte[] encryptedData = CredentialEncryption.encrypt(credentials, secretKey);
            saveToFile(encryptedData);
        }
        catch (CredentialEncryption.EncryptionException | IOException ex) {
            throw new GeneralSecurityException(ex);
        }
    }

    /**
     * Load and decrypt credentials from a file.
     *
     * @param secretKey The SecretKey used for decryption.
     * @return An array containing the username and password.
     * @throws LoginException If an error occurs during decryption or loading.
     */
    public static String[] loadCredentials(SecretKey secretKey) throws LoginException {
        try {
            byte[] encryptedData = readFromFile();
            String decryptedData = CredentialEncryption.decrypt(encryptedData, secretKey);
            return decryptedData.split(":");
        }
        catch (CredentialEncryption.EncryptionException | IOException ex) {
            throw new LoginException();
        }
    }

    /**
     * Save data to a file.
     *
     * @param data The data to save.
     * @throws IOException If an error occurs during saving.
     */
    private void saveToFile(byte[] data) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(CredentialUseCase.CREDENTIALS_FILE)) {
            fos.write(data);
        }
    }

    /**
     * Read data from a file.
     *
     * @return The data read from the file.
     * @throws IOException If an error occurs during reading.
     * @throws FileNotFoundException if no file is found.
     */
    private static byte[] readFromFile() throws IOException, FileNotFoundException {
        File file = new File(CredentialUseCase.CREDENTIALS_FILE);
        if (!file.exists()) {
            throw new FileNotFoundException("File " + CredentialUseCase.CREDENTIALS_FILE + " not found.");
        }

        byte[] data = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(data);
        }
        return data;
    }
}
