package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import network.libraryClient;

public class NewUser {
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private Button New;
    private libraryClient client;
    public void initalize(libraryClient client){
        this.client = client;
    }
    @FXML
    protected void onNewAction(ActionEvent event){

    }

}
