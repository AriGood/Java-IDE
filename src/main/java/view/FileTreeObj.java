package view;

import app.IDEAppBuilder;

import javax.swing.*;
import java.io.File;

public class FileTreeObj {
    private use_case.FileManagement.FileTreeGenerator fileTreeGenerator;
    private File directory;
    private JTree fileTree;

    public FileTreeObj(IDEAppBuilder IDEAppBuilder) {
        chooseDiretory();
        fileTreeGenerator = new use_case.FileManagement.FileTreeGenerator(IDEAppBuilder);
        fileTree = fileTreeGenerator.createFileTree(directory);
    }

    public void chooseDiretory() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a Project to Open");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int option = fileChooser.showOpenDialog(null);

        if (option == JFileChooser.APPROVE_OPTION) {
            directory = fileChooser.getSelectedFile();
        }
    }

    public JTree getFileTree() {
        return fileTree;
    }

    public File getDirectory() {
        return directory;
    }
}
