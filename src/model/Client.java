package model;

import control.ClientThread;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * This class represents the client side of the game;
 * It contains all the controls and methods that client can do on server;
 * Created by suppressf0rce on 4/15/17.
 */
public class Client {

    //Variables
    //------------------------------------------------------------------------------------------------------------------
    /**
     * This represents the client address usually localhost
     */
    private InetAddress clientAddress;

    /**
     * Instance of client side of the socket
     */
    private Socket socket;


    //Constructor
    //------------------------------------------------------------------------------------------------------------------
    public Client(String serverAddress, int port, Window window) {
        try {
            this.clientAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            //Ignore
        }

        //Connect to the server
        try {
            socket = new Socket(serverAddress, port);
            ClientThread thread = new ClientThread(window, socket);
            thread.start();
        } catch (IOException e) {

            //Show user failed connection message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Server Connection");
            alert.setHeaderText("Connection to the TicTacToe server Failed!");
            alert.setContentText("Could not open connection to the server.");

            // Create expandable Exception.
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
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

    }


}
