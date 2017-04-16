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
import model.RegisterRequest;

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

    /**
     * Instance of the client thread
     */
    private ClientThread instance;

    //Constructor
    //------------------------------------------------------------------------------------------------------------------
    public ClientThread(Window window, Socket socket) {
        this.window = window;
        this.socket = socket;
        instance = this;
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

            socket.close();

        } catch (Exception e) {
            interrupt();
        }
    }

    /**
     * This method handles show login request from the server
     *
     * @param brinp      Buffered reader of the socket
     * @param out_socket PrintWriter of the socket
     * @throws Exception Exception in the sockets
     */
    private void showLogin(BufferedReader brinp, PrintWriter out_socket) throws Exception {
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
                showLoginDialog(object, out_socket);
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
        } else if (object.get("requestType").getAsString().equals("register")) {
            System.out.println("Checking for successful registration");
            if (object.get("successful").getAsBoolean()) {
                showLoginDialog(object, out_socket);
            } else {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText(object.get("serverText").getAsString());
                    alert.showAndWait();
                    showRegisterDialog(out_socket);
                });
            }
        }
    }

    /**
     * This method sends login info to the server
     *
     * @param username username of the user
     * @param password password of the user
     * @param out      port of the user
     */
    private void sendLoginInfo(String username, String password, PrintWriter out) {
        LoginRequest request = new LoginRequest();
        request.setPassword(password);
        request.setUsername(username);
        out.println(request.post());
    }

    private void showLoginDialog(JsonObject object, PrintWriter out_socket) {
        Platform.runLater(() -> {
            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Login");
            dialog.setHeaderText(object.get("serverText").getAsString());

            // Set the icon (must be included in the project).
            dialog.setGraphic(new ImageView(this.getClass().getResource("/img/Login.png").toString()));

            // Set the button types.
            ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
            ButtonType registerButtonType = new ButtonType("Register", ButtonBar.ButtonData.APPLY);
            ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, registerButtonType, cancelButtonType);

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
                if (dialogButton == registerButtonType) {
                    showRegisterDialog(out_socket);
                    dialog.close();
                    return null;
                }
                return null;
            });

            Optional<Pair<String, String>> result = dialog.showAndWait();

            result.ifPresent(usernamePassword -> {
                if (!usernamePassword.getKey().isEmpty() && !usernamePassword.getValue().isEmpty()) {
                    System.out.println("Sending info to the server");
                    sendLoginInfo(usernamePassword.getKey(), usernamePassword.getValue(), out_socket);
                }
            });
        });
    }

    /**
     * This methods shows login dialog if user wants to register
     *
     * @param out_socket
     */
    private void showRegisterDialog(PrintWriter out_socket) {
        Platform.runLater(() -> {
            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Register");
            dialog.setHeaderText("Please register to the server");

            // Set the icon (must be included in the project).
            dialog.setGraphic(new ImageView(this.getClass().getResource("/img/Login.png").toString()));

            // Set the button types.
            ButtonType loginButtonType = new ButtonType("Register", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

            // Create the username and password labels and fields.
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField username = new TextField();
            username.setPromptText("Username");
            TextField nickname = new TextField();
            nickname.setPromptText("Nickname");
            PasswordField password = new PasswordField();
            password.setPromptText("Password");
            PasswordField repeatPassword = new PasswordField();
            repeatPassword.setPromptText("Repeat password");

            grid.add(new Label("Username:"), 0, 0);
            grid.add(username, 1, 0);
            grid.add(new Label("Nickname"), 0, 1);
            grid.add(nickname, 1, 1);
            grid.add(new Label("Password:"), 0, 2);
            grid.add(password, 1, 2);
            grid.add(new Label("Repeat Password"), 0, 3);
            grid.add(repeatPassword, 1, 3);

            // Enable/Disable login button depending on whether a username was entered.
            Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
            loginButton.setDisable(true);

            // Do some validation (using the Java 8 lambda syntax).
            username.textProperty().addListener((observable, oldValue, newValue) -> {
                if (username.getText().isEmpty() || nickname.getText().isEmpty() || password.getText().isEmpty())
                    loginButton.setDisable(true);
                else if (password.getText().equals(repeatPassword.getText()))
                    loginButton.setDisable(false);
                else
                    loginButton.setDisable(true);
            });

            nickname.textProperty().addListener((observable, oldValue, newValue) -> {
                if (username.getText().isEmpty() || nickname.getText().isEmpty() || password.getText().isEmpty())
                    loginButton.setDisable(true);
                else if (password.getText().equals(repeatPassword.getText()))
                    loginButton.setDisable(false);
                else
                    loginButton.setDisable(true);
            });

            password.textProperty().addListener((observable, oldValue, newValue) -> {
                if (username.getText().isEmpty() || nickname.getText().isEmpty() || password.getText().isEmpty())
                    loginButton.setDisable(true);
                else if (password.getText().equals(repeatPassword.getText()))
                    loginButton.setDisable(false);
                else
                    loginButton.setDisable(true);
            });

            repeatPassword.textProperty().addListener((observable, oldValue, newValue) -> {
                if (username.getText().isEmpty() || nickname.getText().isEmpty() || password.getText().isEmpty())
                    loginButton.setDisable(true);
                else if (password.getText().equals(repeatPassword.getText()))
                    loginButton.setDisable(false);
                else
                    loginButton.setDisable(true);
            });

            dialog.getDialogPane().setContent(grid);

            // Request focus on the username field by default.
            Platform.runLater(() -> username.requestFocus());

            // Convert the result to a username-password-pair when the login button is clicked.
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == loginButtonType) {
                    return new Pair<>(username.getText(), password.getText());
                }
                if (dialogButton == ButtonType.CANCEL) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    interrupt();
                }
                return null;
            });

            Optional<Pair<String, String>> result = dialog.showAndWait();

            result.ifPresent(usernamePassword -> {
                System.out.println("Sending register info to the server");
                sendRegisterInfo(usernamePassword.getKey(), usernamePassword.getValue(), nickname.getText(), out_socket);
            });
        });
    }

    /**
     * This method sends the server register info
     *
     * @param username   username of the user
     * @param password   password of the user
     * @param nickname   nickname of the user
     * @param out_socket socket output
     */
    private void sendRegisterInfo(String username, String password, String nickname, PrintWriter out_socket) {
        RegisterRequest request = new RegisterRequest();
        request.setUsername(username);
        request.setPassword(password);
        request.setNickname(nickname);
        out_socket.println(request.post());
    }
}
