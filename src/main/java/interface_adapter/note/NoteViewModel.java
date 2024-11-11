package interface_adapter.note;

import interface_adapter.ViewModel;

/**
 * The ViewModel for the IdeView.
 */
public class NoteViewModel extends ViewModel<NoteState> {
    public NoteViewModel() {
        super("note");
        setState(new NoteState());
    }
}
