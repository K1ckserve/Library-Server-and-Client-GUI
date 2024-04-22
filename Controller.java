import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
            Parent root = loader.load();
            MainController mainContr = loader.getController();
            if(client.getCatalog().books.size())
            client.setCatalogUpdateListener(mainContr);
            mainContr.initialize(client, client.getCatalog());

            // Get the current stage
            Stage stage = (Stage) Login.getScene().getWindow();

            // Set the scene to the next FXML file
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Library");
            stage.show();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    // You can add other methods to handle additional GUI actions
}
