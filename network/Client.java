package network;

import javafx.application.Platform;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

import common.*;

public class Client {
    private final ReentrantLock lock = new ReentrantLock();
    public Socket socket;
    public Thread objReader;
    //private PrintWriter writer;
    private Catalog clientCatalog = new Catalog();
    private Catalog catalog = new Catalog();
    private CatalogUpdateListener listener;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private User use;

//what the fuckl

    public Client() throws IOException {
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
    public synchronized void disconnectLogin() throws IOException, InterruptedException {
        oos.writeObject("message");
        oos.writeObject("logout");
        objReader.join();
        catalog = new Catalog();
    }
    public User getUser(){
        return this.use;
    }
    public void connectToServer(String ipAddress, int port) throws IOException {
        socket = new Socket(InetAddress.getLocalHost (), port);
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
    }
    public boolean sendLoginCredentials(String username, String password) throws IOException, ClassNotFoundException {
        oos.writeObject("Login");
        oos.writeObject(username);
        oos.writeObject(password);
        String info = (String)ois.readObject();
        if(info.equals("success")){
            Object o = ois.readObject();
            this.use = (User)o;
            System.out.println(use);
        }
        return (boolean) ois.readObject();
    }
    public void createNewUser(String user, String pass) throws IOException {
        oos.writeObject("New");
        oos.writeObject(user);
        oos.writeObject(pass);
    }
    public void resetPassword(String username, String password) throws IOException, ClassNotFoundException {
        oos.writeObject("Reset");
        oos.writeObject(username);
        oos.writeObject(password);
    }
    public static void main(String[] args) throws IOException {
        new Client().setupNetworking();
    }

    public void setupNetworking() {
        try {
            System.out.println("network established");
            Thread objReader = new Thread(new reciever (socket,clientCatalog,catalog));
            this.objReader = objReader;
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
        Socket socket;//please
        Catalog clientStorage;
        Catalog cat;
        public reciever (Socket socket, Catalog clientStorage, Catalog cat) throws IOException {
            this.socket = socket;
            this.clientStorage = clientStorage;
            this.cat = cat;
        }
        @Override
        public void run() {
            synchronized (this) {
                try {
                    while (true) {
                        Object recievedObject = ois.readObject();
                        if (recievedObject != null) {
                            if (recievedObject instanceof Catalog) {
                                Catalog c = (Catalog) recievedObject;
                                String serOrClient = (String) ois.readObject();
                                if (serOrClient.equals("user")) {
                                    clientStorage.copy(c);
                                    Platform.runLater(() -> {
                                        notifyCatalogClientCatalog();
                                    });
                                } else {
                                    if (!(cat.books.size() == c.books.size() && cat.movies.size() == c.movies.size() && cat.games.size() == c.games.size() && cat.audioBooks.size() == c.audioBooks.size())) {
                                        cat = (Catalog) recievedObject;
                                        Client.this.updateCatalog(cat);
                                        Platform.runLater(() -> {
                                            notifyCatalogUpdate();
                                        });
                                    }
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
                            } else if (recievedObject instanceof AudioBook) {
                                AudioBook audioBooks = (AudioBook) recievedObject;
                                clientStorage.addAudioBook(audioBooks);
                                System.out.println(audioBooks);
                                Platform.runLater(() -> {
                                    notifyCatalogClientCatalog();
                                });
                            }else if (recievedObject instanceof String){
                                if(recievedObject.equals("die")){
                                    break;
                                }
                            }
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
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
