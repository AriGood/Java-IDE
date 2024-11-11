package entity;

import java.io.File;
import use_case.FileManagement.FileOperations;

public class Tab {
    private String tabName;
    private String tabContent;
    public Tab(File file) {
        this.tabName = file.getName();
        this.tabContent = use_case.FileManagement.FileOperations.fileContent(file);
    }
    public String getTabName() {
        return tabName;
    }
    public void setTabName(String tabName) {
        this.tabName = tabName;
    }
}
