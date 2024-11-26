package org.use_case.git;

import org.junit.Test;
import use_case.git.SecureKeyManager;

import javax.crypto.SecretKey;

import static org.junit.Assert.*;

/**
 * Unit tests for the SecureKeyManager class.
 */
public class SecureKeyManagerTest {

    /**
     * Tests the saveKey() method by ensuring no exceptions are thrown.
     */
    @Test
    public void testSaveKey() {
        try {
            SecureKeyManager.saveKey();
        } catch (Exception e) {
            fail("saveKey() threw an exception: " + e.getMessage());
        }
    }

    /**
     * Tests the loadKey() method by verifying the retrieved key is not null.
     */
    @Test
    public void testLoadKey() {
        try {
            // Ensure the key is saved before attempting to load it
            SecureKeyManager.saveKey();

            // Load the key and verify it's not null
            SecretKey key = SecureKeyManager.loadKey();
            assertNotNull("loadKey() returned a null key.", key);
        } catch (Exception e) {
            fail("loadKey() threw an exception: " + e.getMessage());
        }
    }

    /**
     * Tests that the loaded key is consistent with the saved key.
     */
    @Test
    public void testKeyConsistency() {
        try {
            // Save the key
            SecureKeyManager.saveKey();

            // Load the key twice and compare
            SecretKey key1 = SecureKeyManager.loadKey();
            SecretKey key2 = SecureKeyManager.loadKey();

            assertNotNull("First loaded key is null.", key1);
            assertNotNull("Second loaded key is null.", key2);

            // Compare the encoded forms of the keys
            assertArrayEquals("Loaded keys are not consistent.", key1.getEncoded(), key2.getEncoded());
        } catch (Exception e) {
            fail("Key consistency test threw an exception: " + e.getMessage());
        }
    }
}
