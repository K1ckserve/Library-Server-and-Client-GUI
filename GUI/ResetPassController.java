package GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ResetPassController {
    @FXML
    private Button Password;
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    private libraryClient client
    private void initialize(libraryClient client){
        this.client = client;
    }
    protected void resetPassword(ActionEvent event){
        String username.getText();
        String password.getText();
        client.resetPassword(username, password);
        URL url = Paths.get("./GUI/Login.fxml").toUri().toURL();
        FXMLLoader loader = new FXMLLoader(url);
        loader.setLocation(url);
        Parent root = loader.load();
        Controller controller = loader.getController();


        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Client");
        primaryStage.show();
    }


}
