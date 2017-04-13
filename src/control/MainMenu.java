package control;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

/**
 * This class is control class for the MainMenu.fxml<br>
 * It contains all the actions you can do inside it<br>
 * Created by SuppresSF0rcE on 4/13/17.
 */
public class MainMenu {

    //Variables
    //-----------------------------------------------------------------------------------
    /**
     * Client/Server Radio Buttons from the FXML
     */
    @FXML
    private RadioButton rbClient, rbServer;

    //Methods
    //------------------------------------------------------------------------------------

    /**
     * This method handles action on Server radio button selection change
     */
    @FXML
    public void changeToServerConfig() {
        if (rbServer.isSelected()) {

            //Getting reference to the border pane
            BorderPane borderPane = (BorderPane) rbServer.getParent().getParent().getParent();

            //Setting center
            try {
                borderPane.setCenter(FXMLLoader.load(getClass().getResource("/view/ServerConfiguration.fxml")));
            } catch (Exception e) {
                System.out.println("Could not load FXML file: " + e.getMessage());
            }
        }
    }

    /**
     * This method handles action on Client radio button selection change
     */
    @FXML
    public void changeToClientConfig() {
        if (rbClient.isSelected()) {

            //Getting reference to the border pane
            BorderPane borderPane = (BorderPane) rbClient.getParent().getParent().getParent();

            //Setting center
            try {
                borderPane.setCenter(FXMLLoader.load(getClass().getResource("/view/ClientConfiguration.fxml")));
            } catch (Exception e) {
                System.out.println("Could not load FXML file: " + e.getMessage());
            }
        }
    }
}
