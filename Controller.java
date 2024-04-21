import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import network.libraryClient;

public class Controller {
    @FXML
    private Button loginButton;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    private libraryClient client;

    public void initialize(libraryClient client) {
        this.client = client;
    }

    @FXML
    protected void loginButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        client.sendLoginCredentials(username, password);
    }

    // You can add other methods to handle additional GUI actions
}
