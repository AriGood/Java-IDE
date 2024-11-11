package interface_adapter.note;

import use_case.note.NoteInputBoundary;

/**
 * Controller for our Note related Use Cases.
 */
public class IdeController {

    private final NoteInputBoundary noteInteractor;

    public IdeController(NoteInputBoundary noteInteractor) {
        this.noteInteractor = noteInteractor;
    }

    /**
     * Executes the Note related Use Cases.
     * @param note the note to be recorded
     */
    public void execute(String note) {
        if (note != null) {
            noteInteractor.executeSave(note);
        }
        else {
            noteInteractor.executeRefresh();
        }
    }
}