package model;

/**
 * This class represents the player of the TicTacToe game
 * Created by suppressf0rce on 4/16/17.
 */
public class Player {

    //Variables
    //------------------------------------------------------------------------------------------------------------------
    /**
     * Instance of the string that represents the nickname of the player
     */
    private String nickname;

    /**
     * Instance of the <code>ClientStatus</code> that tells on what status is player
     *
     * @see ClientStatus
     */
    private ClientStatus status;

    /**
     * Instance of the integer that tells on which position - rank is the player on the server
     */
    private int numOfWins;


    //Methods
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return nickname;
    }


    //Getters & Setters
    //------------------------------------------------------------------------------------------------------------------
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public ClientStatus getStatus() {
        return status;
    }

    public void setStatus(ClientStatus status) {
        this.status = status;
    }

    public int getNumOfWins() {
        return numOfWins;
    }

    public void setNumOfWins(int numOfWins) {
        this.numOfWins = numOfWins;
    }
}
