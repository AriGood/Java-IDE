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

        //TODO Remove this when done.
//        //The next line is only to make testing easier by setting the default directory to where I keep my Files - Dario
//        //Feel free to use your path for testing
        fileChooser.setCurrentDirectory(new File("D:\\Users\\dario\\IdeaProjects\\puentesd"));

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
