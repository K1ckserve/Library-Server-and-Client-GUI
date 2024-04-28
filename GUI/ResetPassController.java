package GUI;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import network.Client;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

public class ResetPassController {
    @FXML
    private Button Password;
    @FXML
    private TextField usernam;
    @FXML
    private TextField passwor;
    @FXML
    private ProgressBar Meter;
    private Client client;

    void initialize(Client client) {
        this.client = client;

        // Add a listener to the password field to update the meter
        passwor.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                updateMeter(newValue);
            }
        });
    }

    private void updateMeter(String password) {
        // Calculate the strength of the password and update the meter accordingly
        double strength = calculatePasswordStrength(password);
        Meter.setProgress(strength);
    }

    private double calculatePasswordStrength(String password) {
        // Calculate the strength based on the presence of numbers and special characters
        double strength = password.length() / 30.0; // Assuming maximum length of 30 for demonstration

        for (char c : password.toCharArray()) {
            if (Character.isDigit(c) || !Character.isLetterOrDigit(c)) {
                // If a number or special character is found, increment the strength
                strength += 0.05; // You can adjust the increment value as needed
                break; // No need to continue checking if one number or special character is found
            }
        }

        return Math.min(strength, 1.0); // Ensure strength does not exceed 1.0
    }

    @FXML
    protected void resetPassword(ActionEvent event) throws IOException, ClassNotFoundException {
        String username = usernam.getText();
        String password = passwor.getText();

        client.resetPassword(username, password);
        URL url = Paths.get("./GUI/Login.fxml").toUri().toURL();
        FXMLLoader loader = new FXMLLoader(url);
        loader.setLocation(url);
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.reInitialize(client);
        Stage primaryStage = (Stage) Password.getScene().getWindow();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Client");
        primaryStage.show();
    }
}
