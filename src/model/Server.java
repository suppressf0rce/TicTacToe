package model;

import control.database.DBBroker;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import view.Window;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

/**
 * This class represents TicTacToe Server, <br>
 * when user hosts the server instance of this class is created
 * Created by suppressf0rce on 4/15/17.
 */
public class Server {

    //Variables
    //------------------------------------------------------------------------------------------------------------------
    /**
     * an <code>String</code> that represents the name of the server
     */
    private String name;

    /**
     * an <code>integer</code> that holds info about the server port
     */
    private int port;

    /**
     * an instance of <code>DBBroker</code> that holds info about the server database
     *
     * @see DBBroker
     */
    private DBBroker broker;

    /**
     * An instance of TCP server socket
     */
    private ServerSocket serverSocket;

    /**
     * An instance of the <code>InetAddress</code> that holds info about the server addressr
     */
    private InetAddress address;

    /**
     * Instance of the server gui window
     *
     * @see Window
     */
    private Window window;


    //Constructor
    //------------------------------------------------------------------------------------------------------------------
    public Server(String name, int port, DBBroker broker) {
        this.name = name;
        this.port = port;
        this.broker = broker;
        try {
            this.address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            //Ignore
        }

        window = new Window("TicTacToe Server: [" + name + "]", false);
        window.setFXML(getClass().getResource("/view/ServerGUI.fxml"));
        window.show();

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            //Show user error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Server Error");
            alert.setHeaderText("Could not start server: " + name + " on port: " + port);
            alert.setContentText("For more details read more info and contact system administrator");

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
            Platform.exit();
        }
    }
}
