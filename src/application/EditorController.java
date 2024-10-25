package application;

import infrastructure.FileService;
import domain.JavaAutoCompleteService;
import domain.JavaCodeRunnerService;
import domain.JavaSyntaxHighlighterService;

import javax.swing.*;
import javax.swing.text.JTextComponent;

public class EditorController {
    private final JavaAutoCompleteService autoCompleteService;
    private final JavaCodeRunnerService codeRunnerService;
    private final JavaSyntaxHighlighterService syntaxHighlighterService;
    private final FileService fileService;

    public EditorController() {
        this.autoCompleteService = new JavaAutoCompleteService();
        this.codeRunnerService = new JavaCodeRunnerService();
        this.syntaxHighlighterService = new JavaSyntaxHighlighterService();
        this.fileService = new FileService();
    }

    public void handleAutoComplete(JTextComponent textComponent, JPopupMenu popup) {
        autoCompleteService.enableAutoComplete(textComponent, popup);
    }

    public void runCode(JTextComponent editor, JTextArea outputArea) {
        codeRunnerService.runCode(editor.getText(), outputArea);
    }

    public void highlightSyntax(JTextComponent editor) {
        syntaxHighlighterService.highlight(editor);
    }
}
