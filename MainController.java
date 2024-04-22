import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import network.Book;
import network.Catalog;
import network.CatalogUpdateListener;
import network.libraryClient;

public class MainController implements CatalogUpdateListener {
    @FXML
    private Button Return;
    @FXML
    private Button rent;
    @FXML
    private VBox bookVBox;
    private libraryClient client;
    private Catalog clientCatalog;
    public void initialize(libraryClient client, Catalog clientCatalog) {
        this.client = client;
        this.clientCatalog = clientCatalog;
        bookVBox.getChildren().add(new CheckBox("why"));
        createCheckBoxes();
    }
    public void setCatalog(Catalog clientCatalog){
        this.clientCatalog = clientCatalog;
    }
    @Override
    public void onCatalogUpdate(Catalog catalog) {
        for(Book book : client.getCatalog().books){
            CheckBox checkBox = new CheckBox(book.toString());
            bookVBox.getChildren().add(checkBox);
        }
    }
    private void createCheckBoxes(){
        for(Book book : client.getCatalog().books){
            CheckBox checkBox = new CheckBox(book.toString());
            bookVBox.getChildren().add(checkBox);
        }
    }
}
