package model;

/**
 * An interface to hold request methods post / get
 * Created by suppressf0rce on 4/15/17.
 */
public interface Requestable {

    /**
     * In our TicTacToe game server sends get methods
     *
     * @return an json sting of the request
     */
    public String post();

    /**
     * In TicTacToe game client sends post methods
     *
     * @return an json string of the request;
     */
    public String get();

}
