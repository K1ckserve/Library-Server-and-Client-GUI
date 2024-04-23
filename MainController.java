import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import network.*;

import java.awt.event.ActionEvent;

public class MainController implements CatalogUpdateListener {
    @FXML
    private Button Return;
    @FXML
    private Button rent;
    @FXML
    private VBox bookVBox;
    @FXML
    private VBox movieVBox;
    @FXML
    private VBox audioVBox;
    @FXML
    private VBox gameVBox;
    private libraryClient client;
    private Catalog clientCatalog;
    public void initialize(libraryClient client, Catalog clientCatalog) {
        this.client = client;
        this.clientCatalog = clientCatalog;
    }
    public void setCatalog(Catalog clientCatalog){
        this.clientCatalog = clientCatalog;
    }
    @Override
    public void onCatalogUpdate(Catalog catalog) {
        setCatalog(catalog);
        for(Book book : client.getCatalog().books){
            CheckBox checkBox = new CheckBox(book.toString());
            bookVBox.getChildren().add(checkBox);
        }
        for(Movie movie : client.getCatalog().movies){
            CheckBox checkBox = new CheckBox(movie.toString());
            movieVBox.getChildren().add(checkBox);
        }
        for(Game game : client.getCatalog().games){
            CheckBox checkBox = new CheckBox(game.toString());
            gameVBox.getChildren().add(checkBox);
        }
        for(AudioBooks audioBook : client.getCatalog().audioBooks){
            CheckBox checkBox = new CheckBox(audioBook.toString());
            audioVBox.getChildren().add(checkBox);
        }
    }
    public void onRentAction(ActionEvent event) {}
    private void createCheckBoxes(){
        for(Book book : client.getCatalog().books){
            CheckBox checkBox = new CheckBox(book.toString());
            bookVBox.getChildren().add(checkBox);
        }
    }
}
