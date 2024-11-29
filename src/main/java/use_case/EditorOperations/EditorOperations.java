package use_case.EditorOperations;

import app.IDEAppBuilder;
import entity.EditorObj;
import entity.LeftIDEJTabbedPane;
import entity.ParentIDEJTabbedPane;
import entity.RightIDEJTabbedPane;

import java.io.File;

public class EditorOperations {

    public static void closeAbstractTab(IDEAppBuilder ideAppBuilder, File file) {
        EditorObj editorObj = new EditorObj();
        editorObj.setFile(file);
        if (ideAppBuilder.getLeftEditorTabbedPane().isDuplicate(file)) {
            LeftIDEJTabbedPane tabbedPane = ideAppBuilder.getLeftEditorTabbedPane();
            tabbedPane.closeTab(EditorOperations.getFileIndex(tabbedPane, file));
        } else {
            if (ideAppBuilder.getRightEditorTabbedPane() != null) {
                RightIDEJTabbedPane tabbedPane = ideAppBuilder.getRightEditorTabbedPane();
                ideAppBuilder.getRightEditorTabbedPane().closeTab(EditorOperations.getFileIndex(tabbedPane, file));
            }
        }
    }

    public static int getFileIndex(ParentIDEJTabbedPane tabbedPane, File file) {
        for (int i = 0; i < tabbedPane.getEditorObjs().size(); i++) {
            if (tabbedPane.getEditorObjs().get(i).getFile().equals(file)) {
                return i;
            }
        }
        return -1;
    }
}
