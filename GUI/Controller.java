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
import java.net.URL;
import java.nio.file.Paths;

public class Controller {
    @FXML
    private Button Login;
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private Button Logout;
    @FXML
    private Button ForgotPassword;

    private libraryClient client;

    public void initialize(libraryClient client) throws IOException {
        this.client = client;
        client.connectToServer("192.168.1.200", 1025);
    }

    @FXML
    protected void loginButtonAction(ActionEvent event) throws IOException {
        String usernam = username.getText();
        String passwor = password.getText();
        boolean loginSuccessful = false;
        try{
            loginSuccessful = client.sendLoginCredentials(usernam, passwor);
            if (loginSuccessful) {
                client.setupNetworking();
                //FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Main.fxml"));
                URL url = Paths.get("./GUI/Main.fxml").toUri().toURL();
                FXMLLoader loader = new FXMLLoader(url);
                loader.setLocation(url);
                Parent root = loader.load();
                MainController mainContr = loader.getController();
                client.setCatalogUpdateListener(mainContr);
                mainContr.initialize(client);

                // Get the current stage
                Stage stage = (Stage) Login.getScene().getWindow();

                // Set the scene to the next FXML file
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Library");
                stage.show();
            } else {
                // Clear password field and prompt user to try again
                username.clear();
                password.clear();
                // You can also display an error message to the user
                // For example: errorMessageLabel.setText("Incorrect username or password. Please try again.");
            }
        } catch(IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    protected void logoutAction(ActionEvent event) throws IOException, InterruptedException {
        client.disconnectLogin();
    }
    @FXML
    protected void forgotPassword(ActionEvent event) throws IOException {
        URL url = Paths.get("./GUI/ResetPass.fxml").toUri().toURL();
        FXMLLoader loader = new FXMLLoader(url);
        loader.setLocation(url);
        Parent root = loader.load();
        MainController mainContr = loader.getController();
        client.setCatalogUpdateListener(mainContr);
        mainContr.initialize(client);

        // Get the current stage
        Stage stage = (Stage) Login.getScene().getWindow();

        // Set the scene to the next FXML file
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Forgot Password");
        stage.show();
    }
    // You can add other methods to handle additional GUI actions
}
