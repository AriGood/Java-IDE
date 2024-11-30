package entity;

import app.IDEAppBuilder;
import entity.EditorObj;
import use_case.EditorOperations.EditorOperations;
import use_case.FileManagement.FileOperations;
import use_case.PopupMenuOperations.PopupMenuOperations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class ParentIDEJTabbedPane extends JTabbedPane {

    protected List<EditorObj> editorObjs = new ArrayList<>();
    protected static IDEAppBuilder ideAppBuilder;

    public ParentIDEJTabbedPane(IDEAppBuilder appBuilder) {
        this.ideAppBuilder = appBuilder;

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showPopupMenu(e);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showPopupMenu(e);
                }
            }
        });
    }

    private void showPopupMenu(MouseEvent e) {
        JPopupMenu popupMenu = PopupMenuOperations.createTabPopup(this, ideAppBuilder);
        if (popupMenu != null) {
            popupMenu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    public List<EditorObj> getEditorObjs() {
        return editorObjs;
    }

    public IDEAppBuilder getIdeAppBuilder() {
        return ideAppBuilder;
    }
}
