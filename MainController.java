import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import network.Book;
import network.libraryClient;

public class MainController {
    @FXML
    private Button Return;
    @FXML
    private Button rent;
    @FXML
    private VBox bookVBox;
    private libraryClient client;
    public void initialize(libraryClient client) {
        this.client = client;
        bookVBox.getChildren().add(new CheckBox("why"));
        createCheckBoxes();
    }
    private void createCheckBoxes(){
        for(Book book : client.getCatalog().books){
            CheckBox checkBox = new CheckBox(book.toString());
            bookVBox.getChildren().add(checkBox);
        }
    }
}
