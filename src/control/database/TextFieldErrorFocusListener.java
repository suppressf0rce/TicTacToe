package control.database;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

/**
 * This class adds style to the required TextFields.<br>
 * It places error style when they are empty
 * Created by suppressf0rce on 4/14/17.
 */
public class TextFieldErrorFocusListener implements ChangeListener {

    //Variables
    //------------------------------------------------------------------------------------------------------------------
    private TextField textField;


    //Constructor
    //------------------------------------------------------------------------------------------------------------------
    public TextFieldErrorFocusListener(TextField textField) {
        this.textField = textField;
    }

    //Methods
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        if (textField.getText().equals(""))
            textField.getStyleClass().add("error");
        else
            textField.getStyleClass().remove("error");
    }
}
