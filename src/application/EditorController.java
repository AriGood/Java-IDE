package application;

import infrastructure.FileService;
import domain.AutoCompleteService;
import domain.CodeRunnerService;
import domain.SyntaxHighlighterService;

import javax.swing.*;
import javax.swing.text.JTextComponent;

public class EditorController {
    private final AutoCompleteService autoCompleteService;
    private final CodeRunnerService codeRunnerService;
    private final SyntaxHighlighterService syntaxHighlighterService;
    private final FileService fileService;

    public EditorController(AutoCompleteService autoCompleteService,
                            CodeRunnerService codeRunnerService,
                            SyntaxHighlighterService syntaxHighlighterService,
                            FileService fileService) {
        this.autoCompleteService = autoCompleteService;
        this.codeRunnerService = codeRunnerService;
        this.syntaxHighlighterService = syntaxHighlighterService;
        this.fileService = fileService;
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
