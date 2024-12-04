package entity;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import app.IDEAppBuilder;
import data.access.AutoCompleteBst;
import view.PopupMenuHandler;

public abstract class ParentIdeJtabbedPane extends JTabbedPane {

    private static IDEAppBuilder ideAppBuilder;
    private List<EditorObj> editorObjs = new ArrayList<>();

    public ParentIdeJtabbedPane(IDEAppBuilder appBuilder) {
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

        this.addChangeListener(autoCompleteListener -> {
            int currentIndex = getSelectedIndex();
            if (currentIndex != -1) {
                JTextArea currentTextArea = editorObjs.get(currentIndex).getTextArea();
                ideAppBuilder.initializeAutoComplete(AutoCompleteBst.buildWithJavaKeywords(), currentTextArea);
            }
        });
    }

    private void showPopupMenu(MouseEvent tabPopUpListener) {
        JPopupMenu popupMenu = PopupMenuHandler.createTabPopup(this, ideAppBuilder);
        if (popupMenu != null) {
            popupMenu.show(tabPopUpListener.getComponent(), tabPopUpListener.getX(), tabPopUpListener.getY());
        }
    }

    /**
     * Returns the list of EditorObjs associated with each tab.
     * @return {@code List<EditorObj>} of each tab's EditorObj.
     */
    public List<EditorObj> getEditorObjs() {
        return editorObjs;
    }

    /**
     * Returns the IDEAppBuilder associated with this instance of ParentIdeJtabbedPane.
     * @return IDEAppBuilder for this ParentIdeJtabbedPane.
     */
    public IDEAppBuilder getIdeAppBuilder() {
        return ideAppBuilder;
    }
}
