package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class libraryServer {
    public static void main(String[] args) {
        new libraryServer().setupNetworking();
    }
    ArrayList<user> history = new ArrayList<>();

    List<Socket> sockets = new ArrayList<Socket>();

    private void setupNetworking() {
        try {
            ServerSocket server = new ServerSocket(1025);
            while (true) {
                Socket clientSocket = server.accept();
                System.out.println("incoming transmission");

                sockets.add(clientSocket);

                Thread t = new Thread(new ClientHandler(clientSocket));
                t.start();
            }
        } catch (IOException ioe) {}
    }

    class ClientHandler implements Runnable {

        private Socket clientSocket;
        ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }
        public void run() {
            try {
                Book book = (Book)(new ObjectInputStream(clientSocket.getInputStream()).readObject());
                System.out.println("GOT THE BOOK " + book.toString());
            } catch (IOException | ClassNotFoundException ioe) {

            }
        }
    }
}
class Book implements Serializable {
    private String title;
    private String author;
    private int pages;
    private String summary;

    public Book(String title, String author, int pages, String summary) {
        this.title = title;
        this.author = author;
        this.pages = pages;
        this.summary = summary;
    }

    @Override
    public String toString() {
        return this.title + " : " + this.author;
    }
}
class Movie implements Serializable {
    private String title;
    private String director;
    public Movie(String title, String director) {
        this.title = title;
        this.director = director;
    }
    @Override
    public String toString() {
        return this.title + " : " + this.director;
    }
}
class Game implements Serializable {
    private String title;
    private String creator;
    private String genre;
    public Game(String title, String creator, String genrep) {
        this.title = title;
        this.creator = creator;
        this.genre = genre;
    }
    @Override
    public String toString() {
        return this.title + " : " + this.creator;
    }

}
class AudioBooks extends Book implements Serializable{

    public AudioBooks(String title, String author, int pages, String summary) {
        super(title, author, pages, summary);
    }
}
