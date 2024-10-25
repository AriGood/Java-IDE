import presentation.IDE;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Launch the IDE in the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            IDE ide = new IDE();
            ide.setVisible(true); // Make sure the IDE window is visible
        });
    }
}
