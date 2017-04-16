package control;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import model.Player;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class represents the controll class of the Lobby.fxml<br>
 * It contains all the controls inside the lobby
 * Created by suppressf0rce on 4/16/17.
 */
public class Lobby implements Initializable {

    //Variables
    //------------------------------------------------------------------------------------------------------------------
    /**
     * Instance of the client side of the socket;
     */
    private Socket socket;

    /**
     * Instance of the PrintWriter out of socket
     */
    private PrintWriter out_socket;

    /**
     * Instance of BufferedReader to the socket
     */
    private BufferedReader in_socket;


    /**
     * Instance to the FXML of list views
     */
    @FXML
    private ListView<Player> top10List, onlineList;


    //Methods
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void startLobby() {
        LobbyThread thread = new LobbyThread(socket, out_socket, in_socket, this);
        thread.start();
    }


    //Getters & Setters
    //------------------------------------------------------------------------------------------------------------------
    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public PrintWriter getOut_socket() {
        return out_socket;
    }

    public void setOut_socket(PrintWriter out_socket) {
        this.out_socket = out_socket;
    }

    public BufferedReader getIn_socket() {
        return in_socket;
    }

    public void setIn_socket(BufferedReader in_socket) {
        this.in_socket = in_socket;
    }

    public ListView<Player> getTop10List() {
        return top10List;
    }

    public void setTop10List(ListView<Player> top10List) {
        this.top10List = top10List;
    }

    public ListView<Player> getOnlineList() {
        return onlineList;
    }

    public void setOnlineList(ListView<Player> onlineList) {
        this.onlineList = onlineList;
    }
}
