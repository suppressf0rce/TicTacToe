package control;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This class will create custom output stream so it would display console text in the gui
 * Created by suppressf0rce on 4/15/17.
 */
public class CustomOutputStream extends OutputStream {

    //Variables
    //------------------------------------------------------------------------------------------------------------------
    private TextArea textArea;


    //Constructor
    //------------------------------------------------------------------------------------------------------------------
    public CustomOutputStream(TextArea textArea) {
        this.textArea = textArea;
    }

    //Methods
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public void write(int b) throws IOException {
        Platform.runLater(() -> {
            textArea.appendText(String.valueOf((char) b));
            textArea.positionCaret(textArea.getText().length());
        });
    }
}
