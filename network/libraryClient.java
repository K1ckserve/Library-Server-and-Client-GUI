package network;

import javafx.application.Platform;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class libraryClient {
    private Socket socket;
    private PrintWriter writer;
    private Catalog clientCatalog = new Catalog();
    private Catalog catalog = new Catalog();
    private CatalogUpdateListener listener;

//what the fuckl

    public libraryClient() throws IOException {
    }

    public void setCatalogUpdateListener(CatalogUpdateListener listener) {
        this.listener = listener;
    }
    private synchronized void notifyCatalogUpdate() {
        if(listener != null) {
            listener.onCatalogUpdate(catalog);
        }
    }
    private void notifyCatalogClientCatalog(){
        synchronized(this) {
            if(listener != null){
                listener.onClientCatalogUpdate(clientCatalog);
            }
        }
    }
    public void connectToServer(String ipAddress, int port) throws IOException {
        socket = new Socket(ipAddress, port);
        writer = new PrintWriter(socket.getOutputStream(), true);
    }
    public void sendLoginCredentials(String username, String password) throws IOException {
        writer.println(username);
        writer.flush();
        writer.println(password);
        writer.flush();
    }
    public static void main(String[] args) throws IOException {
        new libraryClient().setupNetworking();
    }

    public void setupNetworking() {
        try {
            System.out.println("network established");
            //BufferedReader reader = new BufferedReader((new InputStreamReader(socket.getInputStream())));
            Thread objReader = new Thread(new reciever (socket,clientCatalog,catalog));
            objReader.start();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    public void updateCatalog(Catalog c){
        this.catalog = c;
    }
    public Catalog getCatalog(){
        return catalog;
    }
    public Catalog getClientCatalog(){
        return clientCatalog;
    }
    class reciever implements Runnable {
        Socket socket;
        Catalog clientStorage;
        Catalog cat;
        public reciever (Socket socket, Catalog clientStorage, Catalog cat) throws IOException {
            this.socket = socket;
            this.clientStorage = clientStorage;
            this.cat = cat;
        }
        @Override
        public void run() {
            try {
                //ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                while(true){
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    Object recievedObject =  ois.readObject();
                    if(recievedObject != null) {
                        if (recievedObject instanceof Catalog) {
                            Catalog c = (Catalog) recievedObject;
                            if (!(cat.books.size() == c.books.size() && cat.movies.size() == c.movies.size() && cat.games.size() == c.games.size() && cat.audioBooks.size() == c.audioBooks.size())) {
                                cat = (Catalog) recievedObject;
                                libraryClient.this.updateCatalog(cat);
                                Platform.runLater(() -> {
                                    notifyCatalogUpdate();
                                });
                            }
                        } else if (recievedObject instanceof Book) {
                            Book book = (Book) recievedObject;
                            clientStorage.addBook(book);
                            System.out.println(book);
                            Platform.runLater(() -> {
                                notifyCatalogClientCatalog();
                            });
                        } else if (recievedObject instanceof Movie) {
                            Movie movie = (Movie) recievedObject;
                            clientStorage.addMovie(movie);
                            System.out.println(movie);
                            Platform.runLater(() -> {
                                notifyCatalogClientCatalog();
                            });
                        } else if (recievedObject instanceof Game) {
                            Game game = (Game) recievedObject;
                            clientStorage.addGame(game);
                            System.out.println(game);
                            Platform.runLater(() -> {
                                notifyCatalogClientCatalog();
                            });
                        } else if (recievedObject instanceof AudioBooks) {
                            AudioBooks audioBooks = (AudioBooks) recievedObject;
                            clientStorage.addAudioBook(audioBooks);
                            System.out.println(audioBooks);
                            Platform.runLater(() -> {
                                notifyCatalogClientCatalog();
                            });
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void recieveAnItem(String item, String type){
        writer.println("message");
        writer.flush();
        writer.println(type);
        writer.flush();
        writer.println(item);
        writer.flush();
    }
    public void sendABook(Book book) throws IOException {
        writer.println("object");
        writer.flush();
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(book);
        oos.flush();
    }
    public void sendAMovie(Movie movie) throws IOException {
        writer.println("object");
        writer.flush();
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(movie);
        oos.flush();
    }
    public void sendAGame(Game game) throws IOException {
        writer.println("object");
        writer.flush();
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(game);
        oos.flush();
    }
    public void sendAAudioBook(AudioBooks audioBooks) throws IOException {
        writer.println("object");
        writer.flush();
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(audioBooks);
        oos.flush();

    }

}
class user{
   private String username;
   private String password;
   public user(String username, String password) {
       this.username = username;
       this.password = password;
   }
   public String getUsername() {
       return username;
   }
   public void setUsername(String username) {
       this.username = username;
   }
   public String getPassword() {
       return password;
   }
   public void setPassword(String password) {
       this.password = password;
   }
   @Override
    public String toString() {
       return username + " " + password;
   }
}
