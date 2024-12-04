package app;

import static use_case.FileManagement.FileOperations.saveFile;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import data.access.AutoCompleteBst;
import entity.EditorObj;
import entity.GitManager;
import entity.LeftIdeJtabbedPane;
import entity.RightIdeJtabbedPane;
import use_case.EditorManagement.EditorOperations;
import use_case.autocompleteoperations.AutoCompleteOperations;
import view.AutoCompletePopup;
import view.FileTreeObj;
import view.MenuBarObj;
import view.TerminalObj;

/**
 * Builder for the IDE Application.
 */
public class IDEAppBuilder {
    public static final int HEIGHT = 600;
    public static final int WIDTH = 800;
    public static final int LEFT_RIGHT_DIVIDER = 300;
    public static final int UP_DOWN_DIVIDER = 400;
    private GitManager gitManager = new GitManager();

    private File directory;

    private JScrollPane terminalScrollPane;
    private AutoCompleteOperations autoCompleteOperations;
    private JScrollPane fileScrollPane;
    private FileTreeObj fileTreeObj;
    private JFrame frame;
    private JSplitPane leftRightSplitPane;

    private RightIdeJtabbedPane rightEditorTabbedPane;
    private LeftIdeJtabbedPane leftEditorTabbedPane;

    /**
     * Starts up the application, builds the startup screen.
     * @return the JFrame for the application
     */
    public JFrame build() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("IDE Application");
        frame.setSize(WIDTH, HEIGHT);

        frame.setJMenuBar(makeMenuBar());
        frame.add(makeFilePanel(), BorderLayout.CENTER);

        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                saveOpen();
                frame.setVisible(false);
                frame.dispose();
            }
        });
        return frame;
    }

    /**
     * Builds the IDE once a project is selected.
     */
    public void buildIde() {
        frame.add(makeEditorPanel(), BorderLayout.CENTER);

        frame.add(makeTerminalPanel(), BorderLayout.SOUTH);

        leftRightSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, fileScrollPane, leftEditorTabbedPane);
        leftRightSplitPane.setDividerLocation(LEFT_RIGHT_DIVIDER);

        JSplitPane topBottomSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, leftRightSplitPane,
                terminalScrollPane);
        topBottomSplitPane.setDividerLocation(UP_DOWN_DIVIDER);

        frame.add(topBottomSplitPane, BorderLayout.CENTER);
        frame.revalidate();
    }

    /**
     * Activates auto complete for the editor.
     * @param autoCompleteBst the BST with auto complete suggestions.
     * @param codeEditor the given JTextArea for the selected tab.
     */
    public void initializeAutoComplete(AutoCompleteBst autoCompleteBst, JTextArea codeEditor) {
        AutoCompletePopup suggestionPopup = new AutoCompletePopup();
        autoCompleteOperations = new AutoCompleteOperations(autoCompleteBst);

        codeEditor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                SwingUtilities.invokeLater(() -> {
                    List<String> suggestions = autoCompleteOperations.getSuggestions(codeEditor);
                    suggestionPopup.showSuggestions(codeEditor, suggestions, (textComponent, suggestion) -> {
                        autoCompleteOperations.applySuggestion(textComponent, suggestion);
                    });
                });
            }
        });
    }

    /**
     * Builds the initial file panel for the start screen.
     * @return the initial file JScrollPane to be added to Frame.
     */
    private JScrollPane makeFilePanel() {
        fileScrollPane = new JScrollPane();
        JLabel messageLabel = new JLabel("Select A Project Directory To Get Started", JLabel.CENTER);
        fileScrollPane.setViewportView(messageLabel);
        return fileScrollPane;
    }

    /**
     * Updates the files JScrollPane to show a file tree of the given directory.
     * @param newDirectory the selected project directory.
     */
    public void buildTree(File newDirectory) {
        this.directory = newDirectory.getAbsoluteFile();
        fileTreeObj = new FileTreeObj(this.directory, this);
        fileScrollPane.setViewportView(fileTreeObj.getFileTree());
    }

    /**
     * Returns the JMenuBar that will be added to Frame.
     * @return JScrollPane containing a MenuBarObj.
     */
    private JMenuBar makeMenuBar() {
        MenuBarObj menuBarObj = new MenuBarObj(this);
        menuBarObj.buildMenu();
        return menuBarObj.getMenuBar();
    }

    /**
     * Returns the Terminal JScrollPane that will be added to Frame.
     * @return JScrollPane containing a TerminalObj.
     */
    private JScrollPane makeTerminalPanel() {
        TerminalObj terminalWindow = new TerminalObj();
        terminalScrollPane = new JScrollPane(terminalWindow);
        return terminalScrollPane;
    }

    /**
     * Returns the JTabbedPane that will be added as the editor in Frame.
     * @return JTabbedPane containing a LeftIdeJTabbedPane.
     */
    public JTabbedPane makeEditorPanel() {
        leftEditorTabbedPane = new LeftIdeJtabbedPane(this);
        return leftEditorTabbedPane;
    }

    /**
     * Adds the selected File to the editor Tabs.
     * @param file The File to be opened in the editor.
     */
    public void openFile(File file) {
        if (file != null && file.exists() && file.isFile()) {
            if (rightEditorTabbedPane == null || !EditorOperations.isDuplicate(file, rightEditorTabbedPane)) {
                EditorOperations.addTab(file, leftEditorTabbedPane);
            }
        }
        else {
            String message = "Invalid file: ";
            if (file != null) {
                message += file.getAbsolutePath();
            }
            else {
                message += "null";
            }
            System.err.println(message);
        }
    }

    public void saveOpen() {
        if (leftEditorTabbedPane != null) {
            for (EditorObj obj : leftEditorTabbedPane.getEditorObjs()) {
                saveFile(obj.getFile(), obj.getContent());
            }
        }
        if (rightEditorTabbedPane != null) {
            for (EditorObj obj : rightEditorTabbedPane.getEditorObjs()) {
                saveFile(obj.getFile(), obj.getContent());
            }
        }
    }

    /**
     * Returns this IDE's working directory.
     * @return File containing the working directory for the IDE.
     */
    public File getDirectory() {
        return directory;
    }

    /**
     * Returns this IDE's working Frame.
     * @return The JFrame that displays this instance of the IDE app.
     */
    public JFrame getFrame() {
        return frame;
    }

    /**
     * Returns this IDE's working terminal pane.
     * @return the terminal JScrollPane for this instance of the IDE.
     */
    public JScrollPane getTerminalScrollPane() {
        return terminalScrollPane;
    }

    /**
     * Sets the Editor to a JSplitPane containing a left and right JTabbedPane.
     * @param newSplit JSplitPane containing the two editor tabbed panes.
     */
    public void splitEditor(JSplitPane newSplit) {
        leftRightSplitPane.setRightComponent(newSplit);
        Component rightTabbedComponent = newSplit.getRightComponent();
        Component leftTabbedComponent = newSplit.getLeftComponent();
        if (rightTabbedComponent instanceof RightIdeJtabbedPane) {
            rightEditorTabbedPane = (RightIdeJtabbedPane) rightTabbedComponent;
        }
        if (leftTabbedComponent instanceof LeftIdeJtabbedPane) {
            leftEditorTabbedPane = (LeftIdeJtabbedPane) leftTabbedComponent;
        }
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Closes the tab of the deleted file.
     * @param file the file that was deleted and is to be removed from the editor.
     */
    public void handleFileDeletion(File file) {
        EditorOperations.closeAbstractTab(this, file);
    }

    /**
     * Returns this IDE's right component of the Editor.
     * @return RightIdeJTabbedPane containing the tabs for the right component of the Editor.
     */
    public RightIdeJtabbedPane getRightEditorTabbedPane() {
        return rightEditorTabbedPane;
    }

    /**
     * Returns this IDE's left component of the Editor when the tabs are split, and the main Editor component otherwise.
     * @return LeftIdeJTabbedPane containing the tabs for the main component of the Editor.
     */
    public LeftIdeJtabbedPane getLeftEditorTabbedPane() {
        return leftEditorTabbedPane;
    }

    /**
     * Returns this IDE's JSplitPane containing the file and editor JScrollPanes.
     * @return JSplitPane containing the file and editor JScrollPanes.
     */
    public JSplitPane getLeftRightSplitPane() {
        return leftRightSplitPane;
    }

    /**
     * Edit the RightEditorTabbedPane to set it back to null when tabs merge.
     * @param newRightPane usually null.
     */
    public void setRightEditorTabbedPane(RightIdeJtabbedPane newRightPane) {
        rightEditorTabbedPane = newRightPane;
    }

    /**
     * Returns this IDE's GitManager.
     * @return GitManager.
     */
    public GitManager getGitManager() {
        return gitManager;
    }

}
