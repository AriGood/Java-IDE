package view;

import app.IDEAppBuilder;
import use_case.FileManagement.FileTreeGenerator;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.io.File;

public class FileTreeObj {
    private final FileTreeGenerator fileTreeGenerator;
    private File directory;
    private JTree fileTree;
    private final IDEAppBuilder appBuilder;


    public FileTreeObj(File directory, IDEAppBuilder appBuilder) {
        this.directory = directory;
        this.fileTreeGenerator = new FileTreeGenerator();
        this.appBuilder = appBuilder;
        this.fileTree = createFileTree(directory);
    }

    private JTree createFileTree(File directory) {
        DefaultMutableTreeNode rootNode = fileTreeGenerator.createNodesFromDirectory(directory);
        fileTree = new JTree(rootNode);

        fileTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) fileTree.getLastSelectedPathComponent();

                if (selectedNode == null) {
                    System.out.println("No file selected.");
                    return;
                }

                // Reconstruct the file path from the selected node
                StringBuilder filePathBuilder = new StringBuilder();
                TreeNode[] nodePath = selectedNode.getPath();
                for (TreeNode node : nodePath) {
                    filePathBuilder.append(node.toString()).append(File.separator);
                }

                String filePath = filePathBuilder.toString().replaceAll(File.separator + "$", "");
                File selectedFile = new File(directory.getParent(), filePath);

                if (selectedFile.exists() && selectedFile.isFile()) {
                    // Pass the selected file to the IDEAppBuilder to open it in a new tab
                    appBuilder.openFile(selectedFile);
                } else {
                    System.out.println("Selected node is not a valid file.");
                }
            }
        });

        return fileTree;
    }

    public void updateTree(File newDirectory) {
        this.directory = newDirectory;
        DefaultMutableTreeNode rootNode = fileTreeGenerator.createNodesFromDirectory(newDirectory);
        ((DefaultTreeModel) fileTree.getModel()).setRoot(rootNode);
    }

    public void chooseDirectory() {
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
