package domain;

import javax.swing.*;
import java.io.File;

public class JavaCodeRunnerService extends CodeRunnerService {
    @Override
    protected String getFileExtension() {
        return ".java";
    }

    @Override
    protected String[] getCompileCommand(File sourceFile) {
        return new String[]{"javac", sourceFile.getName()};
    }
}
