package entity;

import app.IDEAppBuilder;
import data.access.AutoCompleteBst;
import view.PopupMenuOperations;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

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

        this.addChangeListener(e -> {
            int currentIndex = getSelectedIndex();
            if (currentIndex != -1) {
                JTextArea currentTextArea = editorObjs.get(currentIndex).getTextArea();
                ideAppBuilder.initializeAutoComplete(AutoCompleteBst.buildWithJavaKeywords(), currentTextArea);
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
