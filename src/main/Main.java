package main;


import javafx.application.Application;
import javafx.stage.Stage;
import view.Window;

/**
 * <code>public class Main extends Application</code><br>
 * This class is called when you want to start program <br>
 * It is firstly started in the software and contains the main method
 * Created by SuppresSF0rcE on 4/12/17.
 */
public class Main extends Application {


    /**
     * The default method in the class it is firstly initialized and started on the beginning of the application
     *
     * @param args Arguments of the command line, currently they do nothing, can be ignored
     */
    public static void main(String[] args) {

        //Launch JavaFx Application thread
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Window mainWindow = new Window("TicTacToe - Main menu", false);
        mainWindow.setFXML(getClass().getResource("/view/MainMenu.fxml"));
        mainWindow.show();
    }
}
