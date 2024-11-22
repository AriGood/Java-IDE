package use_case.FileManagement;

import entity.Editor;
import view.EditorObj;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

public class TabManagement {
    public ArrayList<JScrollPane> editors;
    public Editor currentEditor;

    public TabManagement() {
        editors = new ArrayList<>();
    }

    public String newTab(File file) {
        EditorObj newEditor = new EditorObj();
        editors.add(new JScrollPane(FileOperations.fileContent(file));
        currentEditor = editors.get(editors.size()-1);
        return (currentEditor.toString());
    }

    public String newTab(String fileName) {
        editors.add(new Editor(fileName,""));
        currentEditor = editors.get(editors.size()-1);
        return (currentEditor.toString());
    }
}
