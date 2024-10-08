package GUI;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import network.*;
import common.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Iterator;

public class MainController implements CatalogUpdateListener {
    @FXML
    private Button Logout1;
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
    @FXML
    private VBox clientbooksVBox;
    @FXML
    private VBox clientmoviesVBox;
    @FXML
    private VBox clientaudiobooksVBox;
    @FXML
    private VBox clientgamesVBox;
    @FXML
    private Text user;
    @FXML
    private TextField searchBar;
    @FXML
    private Button search;
    private MediaPlayer mediaPlayer;


    private Client client;

    public void initialize(Client client) {
        this.client = client;
        user.setText(client.getUser().getUsername() + "'s catalog");
        String soundFile = "./GUI/celly.mp3"; // Specify the path to your sound file
        Media sound = new Media(new File(soundFile).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
    }
    // Method to show alert with details
    @Override
    public void onCatalogUpdate(Catalog catalog) {
        bookVBox.getChildren().clear();
        movieVBox.getChildren().clear();
        gameVBox.getChildren().clear();
        audioVBox.getChildren().clear();
        //setCatalog(catalog);
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
        for(AudioBook audioBook : client.getCatalog().audioBooks){
            CheckBox checkBox = new CheckBox(audioBook.toString());
            audioVBox.getChildren().add(checkBox);
        }
    }
    @Override
    public void onClientCatalogUpdate(Catalog catalog) {
        clientbooksVBox.getChildren().clear();
        clientmoviesVBox.getChildren().clear();
        clientbooksVBox.getChildren().clear();
        clientgamesVBox.getChildren().clear();
        clientaudiobooksVBox.getChildren().clear();
        //setClientCatalog(catalog);
        synchronized (this) {
            for (Book book : client.getClientCatalog().books) {
                CheckBox checkBox = new CheckBox(book.toString());
                clientbooksVBox.getChildren().add(checkBox);
            }
            for (Movie movie : client.getClientCatalog().movies) {
                CheckBox checkBox = new CheckBox(movie.toString());
                clientmoviesVBox.getChildren().add(checkBox);
            }
            for (Game game : client.getClientCatalog().games) {
                CheckBox checkBox = new CheckBox(game.toString());
                clientgamesVBox.getChildren().add(checkBox);
            }
            for (AudioBook audioBook : client.getClientCatalog().audioBooks) {
                CheckBox checkBox = new CheckBox(audioBook.toString());
                clientaudiobooksVBox.getChildren().add(checkBox);
            }
        }
    }

    public void onRentAction(javafx.event.ActionEvent actionEvent) throws IOException {
        mediaPlayer.play();
        for (Node node : bookVBox.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                if (checkBox.isSelected()) {
                    client.recieveAnItem(checkBox.getText(), "book");
                }
            }
        }
        for (Node node : movieVBox.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                if (checkBox.isSelected()) {
                    client.recieveAnItem(checkBox.getText(), "movie");
                }
            }
        }
        for (Node node : gameVBox.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                if (checkBox.isSelected()) {
                    client.recieveAnItem(checkBox.getText(), "game");
                }
            }
        }
        for (Node node : audioVBox.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                if (checkBox.isSelected()) {
                    client.recieveAnItem(checkBox.getText(), "audiobook");
                }
            }
        }
    }
    public void onReturnAction(javafx.event.ActionEvent actionEvent) throws IOException, InterruptedException {
        mediaPlayer.play();
        for (Node node : clientbooksVBox.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                if (checkBox.isSelected()) {
                    Iterator<Book> iterator = client.getClientCatalog().books.iterator();
                    while (iterator.hasNext()) {
                        Book b = iterator.next();
                        if (b.toString().equals(checkBox.getText())) {
                            client.sendAObject(b);
                            iterator.remove(); // Remove the current item using iterator
                            break; // Exit the loop after removing the item
                        }
                    }
                }
            }
        }
        for(Node node : clientmoviesVBox.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                if (checkBox.isSelected()) {
                    Iterator<Movie> iterator = client.getClientCatalog().movies.iterator();
                    while (iterator.hasNext()) {
                        Movie m = iterator.next();
                        if (m.toString().equals(checkBox.getText())) {
                            client.sendAObject(m);
                            iterator.remove();
                            break;
                        }
                    }
                }
            }
        }
        for(Node node : clientgamesVBox.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                if (checkBox.isSelected()) {
                    Iterator<Game> iterator = client.getClientCatalog().games.iterator();
                    while (iterator.hasNext()) {
                        Game g = iterator.next();
                        if (g.toString().equals(checkBox.getText())) {
                            client.sendAObject(g);
                            iterator.remove();
                            break;
                        }
                    }
                }
            }
        }
        for(Node node : clientaudiobooksVBox.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                if (checkBox.isSelected()) {
                    Iterator<AudioBook> iterator = client.getClientCatalog().audioBooks.iterator();
                    while (iterator.hasNext()) {
                        AudioBook a = iterator.next();
                        if (a.toString().equals(checkBox.getText())) {
                            client.sendAObject(a);
                            iterator.remove();
                            break;
                        }
                    }
                }
            }
        }
        onClientCatalogUpdate(client.getClientCatalog());
    }
    @FXML
    protected void logoutAction(ActionEvent event) throws IOException, InterruptedException {
        client.disconnectLogin();
        URL url = Paths.get("GUI/Login.fxml").toUri().toURL();
        FXMLLoader loader = new FXMLLoader(url);
        loader.setLocation(url);
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.reInitialize(client);
        Stage primaryStage = (Stage) Logout1.getScene().getWindow();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Client");
        primaryStage.show();
    }
    @FXML
    private void searchOnAction(ActionEvent event) {
        String searchTerm = searchBar.getText().trim().toLowerCase(); // Get the search term from the text field
        if (searchTerm.isEmpty()) {
            return; // If the search term is empty, do nothing
        }
        selectCheckBoxBySearchTerm(bookVBox.getChildren(), searchTerm);
        selectCheckBoxBySearchTerm(movieVBox.getChildren(), searchTerm);
        selectCheckBoxBySearchTerm(gameVBox.getChildren(), searchTerm);
        selectCheckBoxBySearchTerm(audioVBox.getChildren(), searchTerm);
    }

    private void selectCheckBoxBySearchTerm(ObservableList<Node> nodes, String searchTerm) {
        for (Node node : nodes) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                if (checkBox.getText().toLowerCase().contains(searchTerm)) {
                    checkBox.setSelected(true); // Select the checkbox if its text contains the search term
                    // Expand the parent titled pane of the checkbox
                    TitledPane titledPane = (TitledPane) checkBox.getParent().getParent().getParent().getParent(); // Get the parent titled pane
                    titledPane.setExpanded(true); // Expand the titled pane
                }
            }
        }
    }


}
