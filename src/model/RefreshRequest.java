package model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import main.AES;

import java.util.ArrayList;

/**
 * This class represents the refresh request
 * Created by suppressf0rce on 4/16/17.
 */
public class RefreshRequest extends Request {

    //Variables
    //------------------------------------------------------------------------------------------------------------------
    private ArrayList<Player> top10List;
    private ArrayList<Player> onlinePlayers;

    @Override
    public String post() {
        JsonObject object = new JsonObject();
        object.addProperty("requestType", "refresh");
        return AES.encrypt(object.toString());
    }

    @Override
    public String get() {
        JsonObject object = new JsonObject();
        object.addProperty("requestType", "refresh");

        JsonArray top10 = new JsonArray();
        for (Player player : top10List) {
            JsonObject jsonPlayer = new JsonObject();
            jsonPlayer.addProperty("nickname", player.getNickname());
            jsonPlayer.addProperty("numOfWins", player.getNumOfWins());
            top10.add(jsonPlayer);
        }
        object.add("top10", top10);

        JsonArray jsonOnlinePlayers = new JsonArray();
        for (Player player : onlinePlayers) {
            JsonObject jsonPlayer = new JsonObject();
            jsonPlayer.addProperty("nickname", player.getNickname());
            jsonPlayer.addProperty("status", player.getStatus().toString());
            jsonOnlinePlayers.add(jsonPlayer);
        }
        object.add("onlinePlayers", jsonOnlinePlayers);
        return AES.encrypt(object.toString());
    }


    //Getters & Setters
    //------------------------------------------------------------------------------------------------------------------
    public ArrayList<Player> getTop10List() {
        return top10List;
    }

    public void setTop10List(ArrayList<Player> top10List) {
        this.top10List = top10List;
    }

    public ArrayList<Player> getOnlinePlayers() {
        return onlinePlayers;
    }

    public void setOnlinePlayers(ArrayList<Player> onlinePlayers) {
        this.onlinePlayers = onlinePlayers;
    }
}
