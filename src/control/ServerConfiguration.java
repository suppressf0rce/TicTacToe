package control;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * This class is control class for the ServerConfiguration.fxml<br>
 * It contains configuration settings inside the server<br>
 * Created by SuppresSF0rcE on 4/13/17.
 */
public class ServerConfiguration {

    /**
     * References to the FXML file Text Fields
     */
    @FXML
    private TextField tfServerHost, tfServerPort, tfDatabasePort, tfDatabaseHost, tfDatabaseName, tfUsername;

    /**
     * References to the FXML file Buttons
     */
    @FXML
    private Button btnHostServer, btnTestConnection;

    /**
     * Reference to the FXML file CheckBox
     */
    @FXML
    private CheckBox cbCreateDatabase;

    /**
     * Reference to the FXML file Password Field
     */
    @FXML
    private PasswordField pfPassword;
}
