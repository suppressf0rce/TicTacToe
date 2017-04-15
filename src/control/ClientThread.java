package control;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Window;
import javafx.util.Pair;
import main.AES;
import model.LoginRequest;

import java.io.*;
import java.net.Socket;
import java.util.Optional;

/**
 * This class represents thread that is running on the client
 * Created by suppressf0rce on 4/15/17.
 */
public class ClientThread extends Thread {

    //Variables
    //------------------------------------------------------------------------------------------------------------------
    /**
     * Window reference to the main menu
     */
    private Window window;

    /**
     * Reference to the socket
     */
    private Socket socket;

    /**
     * Boolean that tells whether user is logged in;
     */
    private boolean isLoggedIn;

    /**
     * Instance of json Parser
     */
    private JsonParser parser = new JsonParser();

    //Constructor
    //------------------------------------------------------------------------------------------------------------------
    public ClientThread(Window window, Socket socket) {
        this.window = window;
        this.socket = socket;
    }

    //Methods
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public void run() {
        try {
            BufferedReader in_socket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out_socket = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            while (!isLoggedIn) {
                showLogin(in_socket, out_socket);
            }

        } catch (IOException e) {

        }
    }

    private void showLogin(BufferedReader brinp, PrintWriter out_socket) {
        try {
            System.out.println("cekam poruku od servera");
            String line = brinp.readLine();
            System.out.println("starting decrypt");
            line = AES.decrypt(line);
            System.out.println("Decrypted");
            JsonObject object = (JsonObject) parser.parse(line);
            System.out.println("testing for request type");
            if (object.get("requestType").getAsString().equals("login")) {
                System.out.println("Cehcking for login");
                if (object.has("isLoggedIn")) {
                    isLoggedIn = object.get("isLoggedIn").getAsBoolean();
                    System.out.println("Logged in status: " + isLoggedIn);
                }

                if (!isLoggedIn) {
                    System.out.println("showing dialog");
                    // Create the custom dialog.
                    Platform.runLater(() -> {
                        Dialog<Pair<String, String>> dialog = new Dialog<>();
                        dialog.setTitle("Login Dialog");
                        dialog.setHeaderText(object.get("serverText").getAsString());

                        // Set the icon (must be included in the project).
                        dialog.setGraphic(new ImageView(this.getClass().getResource("/img/Login.png").toString()));

                        // Set the button types.
                        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
                        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

                        // Create the username and password labels and fields.
                        GridPane grid = new GridPane();
                        grid.setHgap(10);
                        grid.setVgap(10);
                        grid.setPadding(new Insets(20, 150, 10, 10));

                        TextField username = new TextField();
                        username.setPromptText("Username");
                        PasswordField password = new PasswordField();
                        password.setPromptText("Password");

                        grid.add(new Label("Username:"), 0, 0);
                        grid.add(username, 1, 0);
                        grid.add(new Label("Password:"), 0, 1);
                        grid.add(password, 1, 1);

                        // Enable/Disable login button depending on whether a username was entered.
                        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
                        loginButton.setDisable(true);

                        // Do some validation (using the Java 8 lambda syntax).
                        username.textProperty().addListener((observable, oldValue, newValue) -> {
                            loginButton.setDisable(newValue.trim().isEmpty());
                        });

                        dialog.getDialogPane().setContent(grid);

                        // Request focus on the username field by default.
                        Platform.runLater(() -> username.requestFocus());

                        // Convert the result to a username-password-pair when the login button is clicked.
                        dialog.setResultConverter(dialogButton -> {
                            if (dialogButton == loginButtonType) {
                                return new Pair<>(username.getText(), password.getText());
                            }
                            return null;
                        });

                        Optional<Pair<String, String>> result = dialog.showAndWait();

                        result.ifPresent(usernamePassword -> {
                            System.out.println("Sending info to the server");
                            sendLoginInfo(usernamePassword.getKey(), usernamePassword.getValue(), out_socket);
                        });
                    });
                } else {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText(null);
                        alert.setContentText(object.get("serverText").getAsString());
                        alert.showAndWait();
                        //TODO: show lobby gui
                        window.hide();
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendLoginInfo(String username, String password, PrintWriter out) {
        LoginRequest request = new LoginRequest();
        request.setPassword(password);
        request.setUsername(username);
        out.println(request.post());
    }
}
