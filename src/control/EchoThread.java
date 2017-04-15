package control;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import control.database.DBBroker;
import jdk.nashorn.internal.parser.JSONParser;
import main.AES;
import model.ClientStatus;
import model.LoginRequest;
import model.Server;

import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This thread represents every socket / client that is connected to the server.
 * Created by suppressf0rce on 4/15/17.
 */
public class EchoThread extends Thread {

    //Variables
    //------------------------------------------------------------------------------------------------------------------
    /**
     * Instance of the client socket
     */
    private Socket socket;

    /**
     * Instance of the db broker
     *
     * @see DBBroker
     */
    private DBBroker broker;

    /**
     * Instance of the server;
     */
    private Server server;

    /**
     * an <code>String</code> that holds info about the client nickname
     */
    private String nickName;

    /**
     * an <code>ClientStatus</code> that holds info about client Status
     *
     * @see ClientStatus
     */
    private ClientStatus clientStatus;

    /**
     * an <code>boolean</code> that tells whether client is logged in or not;
     */
    private boolean loggedIn = false;

    /**
     * an instance of JsonParser
     */
    private JsonParser parser = new JsonParser();


    //Constructor
    //------------------------------------------------------------------------------------------------------------------
    public EchoThread(Socket socket, DBBroker broker, Server server) {
        this.socket = socket;
        this.broker = broker;
        this.server = server;
    }


    //Methods
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public void run() {
        try {
            BufferedReader in_socket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out_socket = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            // ------------------ PROTOCOL ----------------- //

            //Authorize client
            System.out.println("Sending login request for client: " + socket.getInetAddress());
            sendLoginRequest(out_socket, "Welcome to the " + server.getName() + "!\nPlease Login or Register to continue.");
            while (!loggedIn) {
                processLoginRequest(in_socket, out_socket);
            }


        } catch (IOException e) {
            System.out.println("Client: " + socket.getInetAddress() + " disconnected from the server");
            server.getActiveThreads().remove(this);
        }
    }

    private void sendLoginRequest(PrintWriter out, String message) throws IOException {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLoggedIn(false);
        loginRequest.setServerText(message);
        out.println(loginRequest.get());
    }

    private void processLoginRequest(BufferedReader in_socket, PrintWriter out) throws IOException {
        System.out.println("Processing login request from client: " + socket.getInetAddress());
        String line = in_socket.readLine();
        line = AES.decrypt(line);
        JsonObject object = (JsonObject) parser.parse(line);

        if (object.get("requestType").getAsString().equals("login")) {
            broker.openConnection();
            ResultSet result = broker.getData("SELECT * FROM main WHERE username = '" + object.get("username").getAsString() + "' AND password = '" + AES.encrypt(object.get("password").getAsString()) + "'");
            try {
                if (result.next()) {
                    loggedIn = true;
                    LoginRequest request = new LoginRequest();
                    request.setServerText("Welcome to the server: " + server.getName() + "!");
                    request.setLoggedIn(true);
                    nickName = result.getString("nickname");
                    clientStatus = ClientStatus.FREE;
                    System.out.println("Client: " + nickName + " logged in successfully");
                    out.println(request.get());
                } else {
                    System.out.println("Wrong user name and password for client: " + socket.getInetAddress());
                    sendLoginRequest(out, "Wrong username or password for " + server.getName() + "!\nPlease try again.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //Getters & Setters
    //------------------------------------------------------------------------------------------------------------------
    public String getNickName() {
        return nickName;
    }

    public ClientStatus getClientStatus() {
        return clientStatus;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }
}
