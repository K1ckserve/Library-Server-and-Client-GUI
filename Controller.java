import javafx.fxml.FXML;

import java.awt.*;
import java.lang.reflect.Field;

public class Controller {
    public javafx.scene.control.Button Login;
    @FXML
    private javafx.scene.control.TextField username;
    @FXML
    private javafx.scene.control.TextField password;
    @FXML
    protected void loginButtonAction() {
        Login.setText("This worked");
    }
}
