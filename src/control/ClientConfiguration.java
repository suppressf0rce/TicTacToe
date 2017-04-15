package control;

import control.database.TextFieldNumberListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.Client;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class is control class for the ClientConfiguration.fxml<br>
 * It contains configuration settings inside the client<br>
 * Created by SuppresSF0rcE on 4/13/17.
 */
public class ClientConfiguration implements Initializable {

    //Variables
    //------------------------------------------------------------------------------------------------------------------
    /**
     * References to the Host and Port TextFields from the FXML file
     */
    @FXML
    private TextField tfHost, tfPort;

    /**
     * Reference to the Connect Button from the FXML file
     */
    @FXML
    private Button btnConnect;


    //Methods
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tfHost.focusedProperty().addListener(new TextFieldErrorFocusListener(tfHost));
        tfPort.focusedProperty().addListener(new TextFieldErrorFocusListener(tfPort));

        tfPort.textProperty().addListener(new TextFieldNumberListener(tfPort));
    }


    /**
     * This method handles connect to the server action
     */
    @FXML
    public void connectAction() {
        //Check required fields
        if (checkRequiredFields()) {
            new Client(tfHost.getText(), Integer.valueOf(tfPort.getText()), tfHost.getScene().getWindow());
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("Required fields not filled.\nPlease fill all the required fields");
            alert.showAndWait();
        }
    }


    /**
     * This methods check all the required fields
     */
    private boolean checkRequiredFields() {
        if (!tfHost.getText().equals("") && !tfPort.getText().equals(""))
            return true;
        else
            return false;
    }

}
