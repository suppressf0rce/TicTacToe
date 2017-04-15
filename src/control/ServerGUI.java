package control;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.io.PrintStream;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class is control class for the ServerGUI.fxml<br>
 * It contains graphical user interface components for the server<br>
 * Created by suppressf0rce on 4/15/17.
 */
public class ServerGUI implements Initializable {

    //Variables
    //------------------------------------------------------------------------------------------------------------------
    /**
     * An instance to the server console text Area
     */
    @FXML
    private TextArea textArea;


    //Methods
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PrintStream printStream = new PrintStream(new CustomOutputStream(textArea));
        System.setOut(printStream);
        System.setErr(printStream);
    }
}
