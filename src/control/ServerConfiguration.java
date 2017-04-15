package control;

import control.database.DBBroker;
import control.database.TextFieldNumberListener;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import model.Server;

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

    /**
     * This variable tells whether database connection is tested or not
     */
    private boolean isTested = false;

    /**
     * Instance of Database broker
     *
     * @see DBBroker
     */
    private DBBroker broker;



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

        //Allow only numeric inputs for the port
        tfServerPort.textProperty().addListener(new TextFieldNumberListener(tfServerPort));
        tfDatabasePort.textProperty().addListener(new TextFieldNumberListener(tfDatabasePort));
    }
    /**
     * This methods manages the test database connection action on the button
     */
    @FXML
    public void testDatabaseConnectionAction(){
        //Initializing DBBroker
        broker = new DBBroker();

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
            isTested = true;
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
            isTested = false;
        }


        //Close Connection
        broker.closeConnection();
    }

    /**
     * This method handles action when the host server button is pressed
     */
    @FXML
    public void hostServerAction() {

        //Check required fields
        if (!checkRequiredFields()) {
            //Inform user that he has not filled all the fields
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("Required fields not filled.\nPlease fill all the required fields");
            alert.showAndWait();
            return;
        }

        //Check database connection test
        if (!isTested) {
            testDatabaseConnectionAction();
            if (!isTested)
                return;
        }

        //Create database if user wants to
        if (cbCreateDatabase.isSelected()) {

            broker.openConnection();

            if (broker.executeUpdate("CREATE DATABASE " + tfDatabaseName.getText()) && broker.executeUpdate("CREATE TABLE `" + tfDatabaseName.getText() + "`.`main` (\n" +
                    "  `username` VARCHAR(255) NOT NULL,\n" +
                    "  `password` TEXT NOT NULL,\n" +
                    "  `nickname` VARCHAR(255) NOT NULL,\n" +
                    "  `numOfWins` INT NULL,\n" +
                    "  PRIMARY KEY (`username`),\n" +
                    "  UNIQUE INDEX `username_UNIQUE` (`username` ASC),\n" +
                    "  UNIQUE INDEX `nickname_UNIQUE` (`nickname` ASC));\n")) {
                broker.setDATABASE(tfDatabaseName.getText());
                broker.closeConnection();
            } else {

                //Show user error message
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Database Error");
                alert.setHeaderText("Could not create database:" + tfDatabaseName.getText() + " for user: " + tfUsername.getText());
                alert.setContentText("For more details read more info and contact database administrator");

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

            broker.closeConnection();
            isTested = false;
            return;
        }

        //Start hosting server
        new Server(tfServerName.getText(), Integer.valueOf(tfServerPort.getText()), broker);
        tfServerPort.getScene().getWindow().hide();
    }

    /**
     * This method checks all the required fields<br>
     * If the fields are filled then returns true if not false
     *
     * @return boolean that tells whether all fields are filled or not
     */
    private boolean checkRequiredFields() {
        if (!tfServerName.getText().equals("") &&
                !tfServerHost.getText().equals("") &&
                !tfServerPort.getText().equals("") &&
                !tfDatabaseName.getText().equals("") &&
                !tfDatabaseHost.getText().equals("") &&
                !tfDatabasePort.getText().equals("") &&
                !tfUsername.getText().equals(""))
            return true;
        else
            return false;
    }
}
