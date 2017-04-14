package control;

import control.database.DBBroker;
import control.database.TextFieldErrorFocusListener;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * This class is control class for the ServerConfiguration.fxml<br>
 * It contains configuration settings inside the server<br>
 * Created by SuppresSF0rcE on 4/13/17.
 */
public class ServerConfiguration {

    //Variables
    //------------------------------------------------------------------------------------------------------------------
    /**
     * References to the FXML file Text Fields
     */
    @FXML
    private TextField tfServerName, tfServerHost, tfServerPort, tfDatabasePort, tfDatabaseHost, tfDatabaseName, tfUsername;

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


    //Methods
    //------------------------------------------------------------------------------------------------------------------
    @FXML
    public void initialize() {
        //Adding required text fields listeners
        tfServerName.focusedProperty().addListener(new TextFieldErrorFocusListener(tfServerName));
        tfServerHost.focusedProperty().addListener(new TextFieldErrorFocusListener(tfServerHost));
        tfServerPort.focusedProperty().addListener(new TextFieldErrorFocusListener(tfServerPort));
        tfDatabaseName.focusedProperty().addListener(new TextFieldErrorFocusListener(tfDatabaseName));
        tfDatabaseHost.focusedProperty().addListener(new TextFieldErrorFocusListener(tfDatabaseHost));
        tfDatabasePort.focusedProperty().addListener(new TextFieldErrorFocusListener(tfDatabasePort));
        tfUsername.focusedProperty().addListener(new TextFieldErrorFocusListener(tfUsername));
    }
    /**
     * This methods manages the test database connection action on the button
     */
    @FXML
    public void testDatabaseConnectionAction(){
        //Initializing DBBroker
        DBBroker broker = new DBBroker();

        //Getting data from the text fields
        broker.setHOST(tfDatabaseHost.getText());
        broker.setPORT(tfDatabasePort.getText());
        broker.setUSERNAME(tfUsername.getText());
        broker.setPASSWORD(pfPassword.getText());
        if(!cbCreateDatabase.isSelected())
            broker.setDATABASE(tfDatabaseName.getText());

        //Test connection
        if(broker.testConnection()){

            //Show user successful connection message;
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Database Connection");
            alert.setHeaderText(null);
            alert.setContentText("Successful connection to the database server!");
            alert.showAndWait();
        }else{

            //Show user failed connection message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Connection");
            alert.setHeaderText("Test connection to database failed!");
            alert.setContentText("Could not open connection to the database.");

            // Create expandable Exception.
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            broker.getLastException().printStackTrace(pw);
            String exceptionText = sw.toString();

            Label label = new Label("The exception stacktrace was:");

            TextArea textArea = new TextArea(exceptionText);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);

            // Set expandable Exception into the dialog pane.
            alert.getDialogPane().setExpandableContent(expContent);

            //Ugly hotfix to the FX Alert bug on the linux OS
            //https://bugs.openjdk.java.net/browse/JDK-8087981
            alert.getDialogPane().expandedProperty().addListener((l) -> {

                Platform.runLater(() -> {
                    alert.getDialogPane().requestLayout();
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.sizeToScene();
                });
            });
            alert.showAndWait();
        }


        //Close Connection
        broker.closeConnection();
    }
}
