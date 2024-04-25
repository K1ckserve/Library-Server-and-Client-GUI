package network;

import javafx.application.Platform;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class libraryClient {
    public Socket socket;
    //private PrintWriter writer;
    private Catalog clientCatalog = new Catalog();
    private Catalog catalog = new Catalog();
    private CatalogUpdateListener listener;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

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
            Catalog c = new Catalog();
            c.copy(clientCatalog);
            if(listener != null){
                listener.onClientCatalogUpdate(c);
            }
        }
    }
    public void connectToServer(String ipAddress, int port) throws IOException {
        socket = new Socket(ipAddress, port);
        //writer = new PrintWriter(socket.getOutputStream(), true);
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
    }
    public void sendLoginCredentials(String username, String password) throws IOException {
        oos.writeObject(username);
        oos.writeObject(password);
    }
    public static void main(String[] args) throws IOException {
        new libraryClient().setupNetworking();
    }

    public void setupNetworking() {
        try {
            System.out.println("network established");
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
                while(true){
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
    public synchronized void recieveAnItem(String item, String type) throws IOException {
        oos.writeObject("message");
        oos.flush();
        oos.writeObject(type);
        oos.flush();
        oos.writeObject(item);
        oos.flush();
    }
    public synchronized void sendAObject(Object book) throws IOException, InterruptedException {
        oos.reset();
        oos.writeObject("object");
        oos.flush();
        oos.writeObject(book.toString());
        oos.writeObject(book);
        oos.flush();
    }
}
