package model;

import com.google.gson.JsonObject;
import main.AES;

/**
 * This class represents the register request
 * Created by suppressf0rce on 4/16/17.
 */
public class RegisterRequest extends Request {
    //Variables
    //------------------------------------------------------------------------------------------------------------------
    private String username;
    private String password;
    private String nickname;
    private String serverText;
    private boolean successful;


    //Methods
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public String post() {
        JsonObject object = new JsonObject();
        object.addProperty("requestType", "register");
        object.addProperty("username", username);
        object.addProperty("password", password);
        object.addProperty("nickname", nickname);
        return AES.encrypt(object.toString());
    }

    @Override
    public String get() {
        JsonObject object = new JsonObject();
        object.addProperty("requestType", "register");
        object.addProperty("serverText", serverText);
        object.addProperty("successful", successful);
        return AES.encrypt(object.toString());
    }


    //Getters & Setters
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

    public boolean getSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }
}
