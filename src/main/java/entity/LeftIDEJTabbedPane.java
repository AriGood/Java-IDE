package entity;

import app.IDEAppBuilder;
import data_access.AutoCompleteBST;

import javax.swing.*;

public class LeftIDEJTabbedPane extends ParentIDEJTabbedPane {

    public LeftIDEJTabbedPane(IDEAppBuilder ideAppBuilder) {
        super(ideAppBuilder);

        this.addChangeListener(e -> {
            int currentIndex = getSelectedIndex();
            if (currentIndex != -1) {
                JTextArea currentTextArea = editorObjs.get(currentIndex).getTextArea();
                ideAppBuilder.initializeAutoComplete(AutoCompleteBST.buildWithJavaKeywords(), currentTextArea);
            }
        });
    }

    @Override
    protected JPopupMenu createPopupMenu(int selectedIndex) {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem closeAllTabs = new JMenuItem("Close All Tabs");
        JMenuItem closeOtherTabs = new JMenuItem("Close Other Tabs");
        JMenuItem closeTabsToTheLeft = new JMenuItem("Close Tabs To The Left");
        JMenuItem closeTabsToTheRight = new JMenuItem("Close Tabs To The Right");
        JMenuItem splitTab = new JMenuItem("Split Tab");

        closeAllTabs.addActionListener(e -> closeAllTabs());
        closeOtherTabs.addActionListener(e -> closeOtherTabs(selectedIndex));
        closeTabsToTheLeft.addActionListener(e -> closeTabsToLeft(selectedIndex));
        closeTabsToTheRight.addActionListener(e -> closeTabsToRight(selectedIndex));
        splitTab.addActionListener(e -> splitTab(selectedIndex));

        popupMenu.add(closeAllTabs);
        popupMenu.add(closeOtherTabs);
        popupMenu.add(closeTabsToTheLeft);
        popupMenu.add(closeTabsToTheRight);
        popupMenu.add(splitTab);
        popupMenu.setVisible(true);

        return popupMenu;
    }

    private void splitTab(int selectedIndex) {
        EditorObj editorObj = editorObjs.get(selectedIndex);
        RightIDEJTabbedPane newTabbedPane = new RightIDEJTabbedPane(ideAppBuilder);
        newTabbedPane.addTab(editorObj.getFile());
        closeTab(selectedIndex);
        if (ideAppBuilder.getRightEditorTabbedPane() != null) {
            ideAppBuilder.getRightEditorTabbedPane().addTab(editorObj.getFile());
        } else {
            JSplitPane newSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this, newTabbedPane);
            ideAppBuilder.splitEditor(newSplitPane);
        }
    }
}