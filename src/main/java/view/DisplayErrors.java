package view;

import javax.swing.*;

public class DisplayErrors {

    private DisplayErrors() {
        throw new IllegalStateException("Utility class");
    }

    public static void displayFileLoadError(Exception e) {
        JOptionPane.showMessageDialog(null, "File Could Not Load: " + e.getMessage(),
                "File Load Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void displayFileCopyError(Exception e) {
        JOptionPane.showMessageDialog(null, "Error copying file: " + e.getMessage(),
                "Copying Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void displayFileDeleteError(String name) {
        JOptionPane.showMessageDialog(null, "Failed to delete file: " + name,
                "Deleting Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void displayFileSaveError(Exception e) {
        JOptionPane.showMessageDialog(null, "File could not be saved: " + e.getMessage(),
                "Saving Error", JOptionPane.ERROR_MESSAGE);
    }
}
