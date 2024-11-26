package org.use_case.git;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import use_case.git.CredentialUseCase;

import java.io.File;

import static org.junit.Assert.assertArrayEquals;

public class CredentialUseCaseTest {

    private static final String TEST_USERNAME = "testUser";
    private static final String TEST_PASSWORD = "testPassword";

    // Files to clean up after tests
    private static final String CREDENTIALS_FILE = "credentials.dat";
    private static final String SALT_FILE = "salt.dat";

    private CredentialUseCase credentialUseCase;

    @Before
    public void setUp() {
        credentialUseCase = new CredentialUseCase();
        deleteTestFiles(); // Ensure no leftover test data
    }

    @After
    public void tearDown() {
        deleteTestFiles(); // Clean up test files after each test
    }

    @Test
    public void testSaveAndLoadCredentials() throws Exception {
        // Act
        credentialUseCase.saveCredentials(TEST_USERNAME, TEST_PASSWORD);

        // Assert save
        File credentialsFile = new File(CREDENTIALS_FILE);
        File saltFile = new File(SALT_FILE);
        assert credentialsFile.exists();
        assert saltFile.exists();

        // Act: Load credentials
        String[] loadedCredentials = credentialUseCase.loadCredentials(TEST_PASSWORD);

        // Assert: Verify the data
        assertArrayEquals(new String[]{TEST_USERNAME, TEST_PASSWORD}, loadedCredentials);
    }

    private void deleteTestFiles() {
        new File(CREDENTIALS_FILE).delete();
        new File(SALT_FILE).delete();
    }
}
