package view;

import com.sun.istack.internal.Nullable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * <code>public class Window extends Stage</code><br>
 * This class represents the JavaFx Stage (Window).<br>
 * It can be called whenever there is need to display something on screen
 * Created by SuppresSF0rcE on 4/13/17.
 *
 * @see Stage
 */
public class Window extends Stage {

    /**
     * Constructor for the window that creates and initializes the window<br>
     * <b><i>Example code:</i></b><br>
     * <p>
     * <code>
     * Window mainWindow = new Window("Main Window", false);
     * mainWindow.setFXML("/path/to/my/fxml/file.fxml");
     * mainWindow.show();
     * </code>
     *
     * @param title   an <code>String</code> that represents the title of the window
     * @param isModal an <code>boolean</code> that tells whether the window will be modal or not
     * @see #setFXML(URL)
     */
    public Window(String title, boolean isModal) {

        //Sets the title
        this.setTitle(title);

        //Sets appropriate modality
        if (isModal) {
            this.initModality(Modality.APPLICATION_MODAL);
        }

        //Set size
        this.setWidth(400);
        this.setHeight(600);

    }

    /**
     * Constructor for the window that creates and initializes the window<br>
     * <b><i>Example code:</i></b><br>
     * <p>
     * <code>
     * Window mainWindow = new Window("Main Window", false, 800,600);
     * mainWindow.setFXML("/path/to/my/fxml/file.fxml");
     * mainWindow.show();
     * </code>
     *
     * @param title   an <code>String</code> that represents the title of the window
     * @param isModal an <code>boolean</code> that tells whether the window will be modal or not
     * @param width   an width of the Window
     * @param height  an height of the Window
     * @see #setFXML(URL)
     */
    public Window(String title, boolean isModal, double width, double height) {
        //Sets the title
        this.setTitle(title);

        //Sets appropriate modality
        if (isModal) {
            this.initModality(Modality.APPLICATION_MODAL);
        }

        //Set size
        this.setWidth(width);
        this.setHeight(height);
    }


    /**
     * This method loads the FXML view file into the Window and sets the size according to it<br>
     * Can display exception in console if the passed URL is not valid FXML.
     *
     * @param pathToFxml an <code>URL</code> to the FXML file
     */
    public void setFXML(URL pathToFxml) {
        try {
            //Initialize loader
            FXMLLoader loader = new FXMLLoader(pathToFxml);

            //Load from fxml
            Parent root = loader.load();

            //Create scene
            setScene(new Scene(root));

            //Set size to match scene
            this.sizeToScene();
        } catch (IOException e) {
            System.out.println("Could not load FXML file: " + e.getMessage());
        }
    }

}
