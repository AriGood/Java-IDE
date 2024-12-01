package org.use_case.git;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import use_case.git.*;

import javax.crypto.SecretKey;
import java.io.File;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

public class CredentialUseCaseTest {

    private static final String TEST_USERNAME = "testUser";
    private static final String TEST_PASSWORD = "testPassword";
    private static final String CREDENTIALS_FILE = "credentials.dat";

    private CredentialUseCase credentialUseCase;
    private SecretKey secretKey;

    @Before
    public void setUp() throws Exception {
        // Initialize the CredentialUseCase instance
        credentialUseCase = new CredentialUseCase();

        // Generate a test SecretKey
        secretKey = SecureKeyManager.loadKey();

        // Ensure no leftover files exist before tests
        deleteTestFiles();
    }

    @After
    public void tearDown() {
        // Clean up test files after each test
        deleteTestFiles();
    }

    @Test
    public void testSaveAndLoadCredentials() throws Exception {
        // Save the credentials
        credentialUseCase.saveCredentials(TEST_USERNAME, TEST_PASSWORD, secretKey);

        // Check that the credentials file is created
        File credentialsFile = new File(CREDENTIALS_FILE);
        assertTrue("Credentials file should exist after saving.", credentialsFile.exists());

        // Load the credentials
        String[] loadedCredentials = credentialUseCase.loadCredentials(secretKey);

        // Verify the loaded credentials match the original
        assertArrayEquals("Loaded credentials should match the saved ones.",
                new String[]{TEST_USERNAME, TEST_PASSWORD}, loadedCredentials);
    }

    @Test(expected = Exception.class)
    public void testLoadCredentialsWithWrongKey() throws Exception {
        // Save the credentials
        credentialUseCase.saveCredentials(TEST_USERNAME, TEST_PASSWORD, secretKey);
        SecureKeyManager.saveKey();
        // Generate a different SecretKey
        SecretKey wrongKey = SecureKeyManager.loadKey();

        // Attempt to load credentials with the wrong key, expecting an exception
        credentialUseCase.loadCredentials(wrongKey);
    }

    private void deleteTestFiles() {
        // Delete the credentials file after tests
        File credentialsFile = new File(CREDENTIALS_FILE);
        if (credentialsFile.exists()) {
            credentialsFile.delete();
        }
    }
}
