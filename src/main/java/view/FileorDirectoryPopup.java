package view;

import javax.swing.*;

import java.io.File;

// Menu pop up that shows up when you right-click a file or a folder
// if you click file, you get different options than if you click folder...

public class FileorDirectoryPopup {
    private final JPopupMenu popup;

    public FileorDirectoryPopup(File selectedFile) {
        popup = new JPopupMenu();
        addCommonOptions(selectedFile);
    }

    // Options that are for both: copy, paste, new, git, delete
    // in new we have multiple options: createfile, or create Javafile or create

    public void addCommonOptions(File selectedFile) {
        JMenuItem newGeneral = new JMenuItem("New");
        newGeneral.addActionListener(e -> newGeneral());
        popup.add(newGeneral);
    }

    private void newGeneral() {
        //TODO
    }
}
