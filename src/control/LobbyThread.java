package control;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Platform;
import main.AES;
import model.ClientStatus;
import model.Player;
import model.RefreshRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * This thread refreshes lobby in the background process
 * Created by suppressf0rce on 4/16/17.
 */
public class LobbyThread extends Thread {

    //Variables
    //------------------------------------------------------------------------------------------------------------------
    private Socket socket;
    private PrintWriter out_socket;
    private BufferedReader in_socket;
    private Lobby lobby;
    private JsonParser parser = new JsonParser();


    //Constructor
    //------------------------------------------------------------------------------------------------------------------
    public LobbyThread(Socket socket, PrintWriter out_socket, BufferedReader in_socket, Lobby lobby) {
        this.socket = socket;
        this.in_socket = in_socket;
        this.out_socket = out_socket;
        this.lobby = lobby;
    }

    //Methods
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public void run() {
        while (true) {

            RefreshRequest request = new RefreshRequest();
            out_socket.println(request.post());

            try {
                System.out.println("Cekam poruku od servera");
                String line = in_socket.readLine();
                System.out.println("Dobio sam poruku: " + line);
                line = AES.decrypt(line);
                JsonObject object = (JsonObject) parser.parse(line);

                //Clear lobby lists
                Platform.runLater(() -> {
                    lobby.getTop10List().getItems().clear();
                    lobby.getOnlineList().getItems().clear();
                });

                if (object.get("requestType").getAsString().equals("refresh")) {

                    //Fill top 10
                    JsonArray top10 = object.get("top10").getAsJsonArray();
                    for (JsonElement client : top10) {
                        Player player = new Player();
                        player.setNickname(((JsonObject) client).get("nickname").getAsString());
                        player.setNumOfWins(((JsonObject) client).get("numOfWins").getAsInt());
                        Platform.runLater(() -> {
                            lobby.getTop10List().getItems().add(player);
                        });
                    }

                    //Fill online players
                    JsonArray onlinePlayers = object.get("onlinePlayers").getAsJsonArray();
                    for (JsonElement client : onlinePlayers) {
                        Player player = new Player();
                        player.setNickname(((JsonObject) client).get("nickname").getAsString());
                        String status = ((JsonObject) client).get("status").getAsString();
                        player.setStatus(ClientStatus.valueOf(status));
                        Platform.runLater(() -> {
                            lobby.getOnlineList().getItems().add(player);
                        });
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
