package app;

import javax.swing.*;

import interface_adapter.note.NoteController;
import interface_adapter.note.NotePresenter;
import interface_adapter.note.NoteViewModel;
import use_case.note.NoteDataAccessInterface;
import use_case.note.NoteInteractor;
import use_case.note.NoteOutputBoundary;
import view.NoteView;

import java.awt.*;
import java.io.File;

/**
 * Builder for the Note Application.
 */
public class IDEAppBuilder {
    public static final int HEIGHT = 600;
    public static final int WIDTH = 800;

    //Our instance variables.
    private JScrollPane terminalScrollPane;
    private JScrollPane editorScrollPane;
    private JScrollPane fileScrollPane;
    private JTextArea lineNumbersPane;
    private File directory;

    //Starter code instance variables.
    private NoteDataAccessInterface noteDAO;
    private NoteViewModel noteViewModel = new NoteViewModel();
    private NoteView noteView;
    private NoteInteractor noteInteractor;


    //-------------------------------------------------------------------------------------------------------------------------
/* I split The code I integrated my IDE builder into the Starter code,
this section is part of the starter code that I didn't use, but I left it in
as to not break something.
*/
    /**
     * Sets the NoteDAO to be used in this application.
     * @param noteDataAccess the DAO to use
     * @return this builder
     */
    public IDEAppBuilder addNoteDAO(NoteDataAccessInterface noteDataAccess) {
        noteDAO = noteDataAccess;
        return this;
    }

    /**
     * Creates the objects for the Note Use Case and connects the NoteView to its
     * controller.
     * <p>This method must be called after addNoteView!</p>
     * @return this builder
     * @throws RuntimeException if this method is called before addNoteView
     */
    public IDEAppBuilder addNoteUseCase() {
        final NoteOutputBoundary noteOutputBoundary = new NotePresenter(noteViewModel);
        noteInteractor = new NoteInteractor(
                noteDAO, noteOutputBoundary);

        final NoteController controller = new NoteController(noteInteractor);
        if (noteView == null) {
            throw new RuntimeException("addNoteView must be called before addNoteUseCase");
        }
        noteView.setNoteController(controller);
        return this;
    }

    /**
     * Creates the NoteView and underlying NoteViewModel.
     * @return this builder
     */
    public IDEAppBuilder addNoteView() {
        noteViewModel = new NoteViewModel();
        noteView = new NoteView(noteViewModel);
        return this;
    }


    //---------------------------------------------------------------------------------------------------------------------


    /**
     * Builds the application.
     * @return the JFrame for the application
     */
    public JFrame build() {
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("IDE Application");
        frame.setSize(WIDTH, HEIGHT);

        frame.setJMenuBar(makeMenuBar());

        frame.add(makeEditorPanel(), BorderLayout.CENTER);
        frame.add(makeFilePanel(), BorderLayout.WEST);
        frame.add(makeTerminalPanel(), BorderLayout.SOUTH);


        JSplitPane leftRightSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this.fileScrollPane, this.editorScrollPane);
        leftRightSplitPane.setDividerLocation(300);


        JSplitPane topBottomSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, leftRightSplitPane, this.terminalScrollPane);
        topBottomSplitPane.setDividerLocation(400);


        frame.add(topBottomSplitPane, BorderLayout.CENTER);

        frame.setVisible(true);

        // refresh so that the note will be visible when we start the program
        //noteInteractor.executeRefresh();

        return frame;

    }

    private JScrollPane makeFilePanel() {
        use_case.FileManagement.FileTreeGenerator filePane = new use_case.FileManagement.FileTreeGenerator();
        directory = filePane.getDirectory();
        fileScrollPane = filePane.getFileScrollPane();
        return fileScrollPane;
    }

    private JMenuBar makeMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem newFile = new JMenuItem("New File");
        JMenuItem openFile = new JMenuItem("Open");
        JMenuItem saveFile = new JMenuItem("Save");
        fileMenu.add(newFile);
        fileMenu.add(openFile);
        fileMenu.add(saveFile);
        menuBar.add(fileMenu);
        return menuBar;
    }

    private JScrollPane makeTerminalPanel() {
        JTextArea terminal = new JTextArea();
        terminalScrollPane = new JScrollPane(terminal);
        return terminalScrollPane;
    }

    private JScrollPane makeEditorPanel() {
        JTextArea codeEditor = new JTextArea();
        editorScrollPane = new JScrollPane(codeEditor);
        lineNumbersPane = new JTextArea("1\n");
        lineNumbersPane.setEditable(false);
        lineNumbersPane.setBackground(Color.LIGHT_GRAY);
        editorScrollPane.setRowHeaderView(this.lineNumbersPane);
        return editorScrollPane;
        return null;
    }

}
