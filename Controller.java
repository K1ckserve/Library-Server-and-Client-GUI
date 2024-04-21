import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import network.libraryClient;

import java.io.IOException;

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
    protected void loginButtonAction(ActionEvent event) throws IOException {
        String usernam = username.getText();
        String passwor = password.getText();
        try{
            client.connectToServer("192.168.1.151", 1025);
            client.sendLoginCredentials(usernam, passwor);
            client.setupNetworking();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    // You can add other methods to handle additional GUI actions
}
