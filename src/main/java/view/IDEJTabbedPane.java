package view;

import app.IDEAppBuilder;
import data_access.AutoCompleteBST;
import entity.Editor;
import entity.EditorObj;
import use_case.FileManagement.FileOperations;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class IDEJTabbedPane extends JTabbedPane {

    private List<EditorObj> editorObjs = new ArrayList<>();

    public IDEJTabbedPane(IDEAppBuilder appBuilder) {
        this.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    rightClickEvent(e);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    rightClickEvent(e);
                }
            }

            public void rightClickEvent(MouseEvent e) {
                if (e.isPopupTrigger()) {

                    JPopupMenu popupMenu = this.newTabPopUPMenu();
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }

            private JPopupMenu newTabPopUPMenu() {
                JPopupMenu popupMenu = new JPopupMenu();

                JMenuItem closeAllTabs = new JMenuItem("Close All Tabs");
                JMenuItem closeOtherTabs = new JMenuItem("Close Other Tabs");
                JMenuItem closeTabsToTheRight = new JMenuItem("Close Tabs To The Right");
                JMenuItem closeTabsToTheLeft = new JMenuItem("Close Tabs To The Left");
                JMenuItem splitTab = new JMenuItem("Split Tab");

                int selectedIndex = getSelectedIndex();

                closeAllTabs.addActionListener(ev -> closeAllTabs());
                closeOtherTabs.addActionListener(ev -> closeOtherTabs(selectedIndex));
                closeTabsToTheRight.addActionListener(ev -> closeTabsToTheRight(selectedIndex));
                closeTabsToTheLeft.addActionListener(ev -> closeTabsToTheLeft(selectedIndex));
//                splitTab.addActionListener(ev -> splitTab(selectedIndex));

                popupMenu.add(closeAllTabs);
                popupMenu.add(closeOtherTabs);
                popupMenu.add(closeTabsToTheRight);
                popupMenu.add(closeTabsToTheLeft);
                popupMenu.add(splitTab);
                popupMenu.setVisible(true);

                return popupMenu;
            }
        });

        this.addChangeListener(e -> {
            int currentTabIndex = getSelectedIndex();
            if (currentTabIndex != -1) {
                JTextArea currentTextArea = editorObjs.get(currentTabIndex).getTextArea();
                appBuilder.initializeAutoComplete(AutoCompleteBST.buildWithJavaKeywords(), currentTextArea);
            }
        });
    }

    private void closeTabsToTheLeft(int selectedIndex) {
        List<EditorObj> toKeep = new ArrayList<>();
        for (int i = selectedIndex; i < editorObjs.size(); i++) {
            EditorObj editorObj = editorObjs.get(i);
            toKeep.add(editorObj);
        }
        closeAllTabs();
        for (int i = 0; i < toKeep.size(); i++) {
            addTab(toKeep.get(i).getFile());
        }
        setSelectedIndex(0);
    }

    private void closeTabsToTheRight(int selectedIndex) {
        setSelectedIndex(selectedIndex);
        for (int i = editorObjs.size() - 1; i > selectedIndex; i--) {
            setSelectedIndex(i);
            closeTab();
        }
    }

    private void closeOtherTabs(int selectedIndex) {
        EditorObj editorObj = editorObjs.get(selectedIndex);
        closeAllTabs();
        addTab(editorObj.getFile());
    }

    private void closeAllTabs() {
        for (int i = editorObjs.size() - 1; i >= 0; i--) {
            setSelectedIndex(i);
            closeTab();
        }
    }

    private JButton newCloseButton() {
        JButton closeButton = new JButton("x");
        closeButton.setPreferredSize(new Dimension(15, 15));
        closeButton.setMargin(new Insets(0, 0, 5, 0));
        closeButton.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        closeButton.addActionListener(e -> {
            closeTab();
        });
        return closeButton;
    }

    private void closeTab() {
        int currentTabIndex = getSelectedIndex();
        if (currentTabIndex != -1) {
            EditorObj currentEditorObj = editorObjs.get(currentTabIndex);
            use_case.FileManagement.FileOperations.saveFile(currentEditorObj.getFile(), currentEditorObj.getTextArea().getText());
            this.remove(currentTabIndex);
            this.editorObjs.remove(currentTabIndex);
        }
    }

    public void addTab(File file) {
        EditorObj editorObj = new EditorObj();
        editorObj.setFile(file);
        editorObj.setTextArea(FileOperations.fileContent(file));
        if (isDuplicate(editorObj)) {
            return;
        }
        this.editorObjs.add(editorObj);
        JScrollPane newScrollPane = new JScrollPane(editorObj.getTextArea());
        newScrollPane.setRowHeaderView(editorObj.getLineNums());
        this.add(file.getName(), newScrollPane);
        this.setTabComponentAt(this.getTabCount() - 1, createTabHeader(file.getName()));
        this.setSelectedIndex(this.getTabCount() - 1);
    }

    private boolean isDuplicate(EditorObj editorObj) {
        for (int i = 0; i < this.editorObjs.size(); i++) {
            if (this.editorObjs.get(i).getFile().equals(editorObj.getFile())) {
                this.setSelectedIndex(i);
                return true;
            }
        }
        return false;
    }

    private JPanel createTabHeader(String title) {
        JPanel tabHeader = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel(title);
        tabHeader.setOpaque(false);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        tabHeader.add(titleLabel, BorderLayout.WEST);
        tabHeader.add(newCloseButton(), BorderLayout.EAST);
        return tabHeader;
    }

}
