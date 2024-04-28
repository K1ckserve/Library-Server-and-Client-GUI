package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import network.libraryClient;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;

public class ResetPassController {
    @FXML
    private Button Password;
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    private libraryClient client;
    private void initialize(libraryClient client){
        this.client = client;
    }
    protected void resetPassword(ActionEvent event) throws IOException, ClassNotFoundException {
        String username.getText();
        String password.getText();
        client.resetPassword(username, password);
        URL url = Paths.get("./GUI/Login.fxml").toUri().toURL();
        FXMLLoader loader = new FXMLLoader(url);
        loader.setLocation(url);
        Parent root = loader.load();
        Controller controller = loader.getController();


        Scene scene = new Scene(root);
        Stage primaryStage;
        primaryStage.setScene(scene);
        primaryStage.setTitle("Client");
        primaryStage.show();
    }


}
