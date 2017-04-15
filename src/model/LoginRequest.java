package model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import main.AES;

/**
 * This is the login request;
 * Created by suppressf0rce on 4/15/17.
 */
public class LoginRequest extends Request {

    //Variables
    //------------------------------------------------------------------------------------------------------------------
    //Client variables
    private String username;
    private String password;
    private String nickname;

    //Server variables
    private String serverText;
    private boolean isLoggedIn;


    //Methods
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public String post() {
        JsonObject object = new JsonObject();
        object.addProperty("requestType", "login");
        object.addProperty("username", username);
        object.addProperty("password", password);
        return AES.encrypt(object.toString());
    }

    @Override
    public String get() {
        JsonObject object = new JsonObject();
        object.addProperty("requestType", "login");
        object.addProperty("serverText", serverText);
        object.addProperty("isLoggedIn", isLoggedIn);
        object.addProperty("nickname", nickname);
        return AES.encrypt(object.toString());
    }


    //Getters & Setters
    //------------------------------------------------------------------------------------------------------------------
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getServerText() {
        return serverText;
    }

    public void setServerText(String serverText) {
        this.serverText = serverText;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }
}
