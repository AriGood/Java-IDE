package use_case.FileManagement;

import entity.Editor;

import java.io.File;
import java.util.ArrayList;

public class TabManagement {
    public ArrayList<Editor> editors;
    public Editor currentEditor;

    public TabManagement() {
        editors = new ArrayList<>();
    }

    public String newTab(File file) {
        editors.add(new Editor(file));
        currentEditor = editors.get(editors.size()-1);
        return (currentEditor.toString());
    }
}
