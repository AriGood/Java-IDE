package entity;

import app.IDEAppBuilder;
import data_access.AutoCompleteBST;
import use_case.FileManagement.FileOperations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RightIDEJTabbedPane extends ParentIDEJTabbedPane {

    public RightIDEJTabbedPane(IDEAppBuilder ideAppBuilder) {
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
        JMenuItem mergeTab = new JMenuItem("Merge Tab");

        closeAllTabs.addActionListener(e -> closeAllTabs());
        closeOtherTabs.addActionListener(e -> closeOtherTabs(selectedIndex));
        closeTabsToTheLeft.addActionListener(e -> closeTabsToLeft(selectedIndex));
        closeTabsToTheRight.addActionListener(e -> closeTabsToRight(selectedIndex));
        mergeTab.addActionListener(e -> mergeTab(selectedIndex));

        popupMenu.add(closeAllTabs);
        popupMenu.add(closeOtherTabs);
        popupMenu.add(closeTabsToTheLeft);
        popupMenu.add(closeTabsToTheRight);
        popupMenu.add(mergeTab);
        popupMenu.setVisible(true);

        return popupMenu;
    }

    private void mergeTab(int selectedIndex) {
        setSelectedIndex(selectedIndex);
        EditorObj editorObj = editorObjs.get(selectedIndex);
        closeTab(selectedIndex);
        ideAppBuilder.getLeftEditorTabbedPane().addTab(editorObj.getFile());
        if (editorObjs.isEmpty()) {
            ideAppBuilder.getLeftRightSplitPane().setRightComponent(ideAppBuilder.getLeftEditorTabbedPane());
            ideAppBuilder.setRightEditorTabbedPane(null);
        }
        ideAppBuilder.getFrame().revalidate();
        ideAppBuilder.getFrame().repaint();

    }
}
