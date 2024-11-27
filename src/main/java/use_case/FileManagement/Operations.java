package use_case.FileManagement;

import java.io.File;

public abstract class Operations {
    protected File target;

    public Operations(File target) {
        this.target = target;
    }

    public abstract void copy(File destination);

    public abstract void paste(File destination);

    public abstract void delete();

    public void rename(String newName) {
        File renamedFile = new File(target.getParent(), newName);
        if (target.renameTo(renamedFile)) {
            System.out.println("File renamed to " + renamedFile.getName());
        } else {
            System.err.println("Rename failed for " + target.getName());
        }
    }
}
