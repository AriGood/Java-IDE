package view;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import interface_adapter.note.IdeController;
import interface_adapter.note.NoteState;
import interface_adapter.note.NoteViewModel;

/**
 * The View for when the user is viewing a note in the program.
 */
public class IdeView extends JPanel implements ActionListener, PropertyChangeListener {

    private final JTextArea ideInputField = new JTextArea();

    private final JButton saveButton = new JButton("Save");
    private final JButton refreshButton = new JButton("Refresh");
    private IdeController ideController;

    public IdeView(NoteViewModel noteViewModel) {

        JLabel ideName = new JLabel("note for jonathan_calver2");
        ideName.setAlignmentX(Component.CENTER_ALIGNMENT);
        noteViewModel.addPropertyChangeListener(this);

        final JPanel buttons = new JPanel();
        buttons.add(saveButton);
        buttons.add(refreshButton);

        saveButton.addActionListener(
                evt -> {
                    if (evt.getSource().equals(saveButton)) {
                        ideController.execute(ideInputField.getText());

                    }
                }
        );

        refreshButton.addActionListener(
                evt -> {
                    if (evt.getSource().equals(refreshButton)) {
                        ideController.execute(null);

                    }
                }
        );

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(ideName);
        this.add(ideInputField);
        this.add(buttons);
    }

    /**
     * React to a button click that results in evt.
     * @param evt the ActionEvent to react to
     */
    public void actionPerformed(ActionEvent evt) {
        System.out.println("Click " + evt.getActionCommand());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final NoteState state = (NoteState) evt.getNewValue();
        setFields(state);
        if (state.getError() != null) {
            JOptionPane.showMessageDialog(this, state.getError(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setFields(NoteState state) {
        ideInputField.setText(state.getNote());
    }

    public void setNoteController(IdeController controller) {
        this.ideController = controller;
    }
}

