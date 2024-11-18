package entity;

import java.io.File;

public class Editor {
    private String tabName;
    private String tabContent;

    public Editor(File file) {
        this.tabName = file.getName();
        this.tabContent = use_case.FileManagement.FileOperations.fileContent(file);
    }

    public Editor(String tabName, String tabContent) {
        this.tabName = tabName;
        this.tabContent = tabContent;
    }

    @Override
    public String toString() {
        return this.tabContent;
    }

    public void setTabContent(String tabContent) {
        this.tabContent = tabContent;
    }

    public String getTabName() {
        return tabName;
    }
    public void setTabName(String tabName) {
        this.tabName = tabName;
    }
}
