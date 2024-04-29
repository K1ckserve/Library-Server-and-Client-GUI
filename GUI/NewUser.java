package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import network.Client;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

public class NewUser {
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private Button New;
    private Client client;
    public void initialize(Client client){
        this.client = client;
    }
    @FXML
    protected void onNewAction(ActionEvent event) throws IOException {
        String use = username.getText();
        String pass = password.getText();
        client.createNewUser(use, pass);
        URL url = Paths.get("GUI/Login.fxml").toUri().toURL();
        FXMLLoader loader = new FXMLLoader(url);
        loader.setLocation(url);
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.reInitialize(client);
        Stage primaryStage = (Stage) New.getScene().getWindow();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Client");
        primaryStage.show();

    }

}
