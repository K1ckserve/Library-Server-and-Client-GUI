package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import network.*;
import common.*;

import java.io.IOException;
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
    private libraryClient client;

    public void initialize(libraryClient client) {
        this.client = client;
        user.setText(client.getUser().toString() + "'s catalog");
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
    }
}
