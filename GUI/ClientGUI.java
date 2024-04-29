package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import network.Client;

import java.net.URL;
import java.nio.file.Paths;

public class ClientGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println(getClass().getClassLoader().getResource("./GUI/Login.fxml"));
        URL url = Paths.get("./GUI/Login.fxml").toUri().toURL();
//        FXMLLoader fxmlLoader = new FXMLLoader();
//        fxmlLoader.setLocation(getClass().getResource("Login.fxml"));

        FXMLLoader loader = new FXMLLoader(url);
        loader.setLocation(url);
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
