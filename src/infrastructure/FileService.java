package infrastructure;

import javax.swing.*;
import java.io.*;

public class FileService {

    public void saveFile(JTextPane editor, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(editor.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFile(JTextPane editor, String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            editor.read(reader, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
