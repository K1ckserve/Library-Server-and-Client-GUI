package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import network.Client;

import static com.sun.javafx.scene.control.skin.Utils.getResource;

public class ClientGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println(ClientGUI.class.getResource("Login.fxml"));
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("GUI.Login.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        Client client = new Client();
        controller.initialize(client);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Client");
        primaryStage.show();
    }
}
