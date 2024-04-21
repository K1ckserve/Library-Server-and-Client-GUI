import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import network.libraryClient;

public class Controller {
    @FXML
    private Button Login;
    @FXML
    private TextField username;
    @FXML
    private TextField password;

    private libraryClient client;

    public void initialize(libraryClient client) {
        this.client = client;
    }

    @FXML
    protected void loginButtonAction(ActionEvent event) {
        String usernam = username.getText();
        String passwor = password.getText();
        client.sendLoginCredentials(usernam, passwor);
    }

    // You can add other methods to handle additional GUI actions
}
