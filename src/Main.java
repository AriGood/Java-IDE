package presentation;

import application.EditorController;
import domain.JavaAutoCompleteService;
import domain.JavaCodeRunnerService;
import domain.JavaSyntaxHighlighterService;
import infrastructure.FileService;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Initialize language-specific services
            var autoCompleteService = new JavaAutoCompleteService();
            var codeRunnerService = new JavaCodeRunnerService();
            var syntaxHighlighterService = new JavaSyntaxHighlighterService();
            var fileService = new FileService();

            // Create EditorController with language-specific services
            var editorController = new EditorController(autoCompleteService, codeRunnerService, syntaxHighlighterService, fileService);

            // Create and display the IDE
            IDE ide = new IDE(editorController);
            ide.setVisible(true);
        });
    }
}
