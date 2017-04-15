package control.database;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

/**
 * Created by suppressf0rce on 4/15/17.
 */
public class TextFieldNumberListener implements ChangeListener<String> {

    //Variables
    //------------------------------------------------------------------------------------------------------------------
    private TextField textField;

    //Constructor
    //------------------------------------------------------------------------------------------------------------------
    public TextFieldNumberListener(TextField textField) {
        this.textField = textField;
    }

    //Methods
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (newValue.matches("\\d*")) {
            try {
                int value = Integer.parseInt(newValue);
            } catch (NumberFormatException e) {
                //Ignore
            }
        } else {
            textField.setText(oldValue);
        }
    }
}
