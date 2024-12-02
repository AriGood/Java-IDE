package entity;

import app.IDEAppBuilder;
import entity.EditorObj;
import use_case.FileManagement.FileOperations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class ParentIDEJTabbedPane extends JTabbedPane {

    public List<EditorObj> editorObjs = new ArrayList<>();
    protected IDEAppBuilder ideAppBuilder;

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
        JPopupMenu popupMenu = createPopupMenu(getSelectedIndex());
        if (popupMenu != null) {
            popupMenu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    protected abstract JPopupMenu createPopupMenu(int selectedIndex);

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
        tabHeader.add(createCloseButton(), BorderLayout.EAST);
        return tabHeader;
    }

    private JButton createCloseButton() {
        JButton closeButton = new JButton("x");
        closeButton.setPreferredSize(new Dimension(15, 15));
        closeButton.setMargin(new Insets(0, 0, 5, 0));
        closeButton.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        closeButton.addActionListener(e -> closeTab(getSelectedIndex()));
        return closeButton;
    }

    protected void closeTab(int index) {
        if (index != -1) {
            EditorObj editorObj = editorObjs.get(index);
            FileOperations.saveFile(editorObj.getFile(), editorObj.getTextArea().getText());
            this.remove(index);
            this.editorObjs.remove(index);
        }
    }

    public void closeAllTabs() {
        while (this.getTabCount() > 0) {
            closeTab(0);
        }
    }

    public void closeOtherTabs(int index) {
        if (index != -1) {
            EditorObj keepEditor = editorObjs.get(index);
            closeAllTabs();
            addTab(keepEditor.getFile());
        }
    }

    public void closeTabsToLeft(int index) {
        while (index > 0) {
            closeTab(0);
            index--;
        }
    }

    public void closeTabsToRight(int index) {
        while (this.getTabCount() > index + 1) {
            closeTab(index + 1);
        }
    }
}
