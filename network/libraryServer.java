package network;

import java.io.*;
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
    serverStorage ss = new serverStorage();

    private void setupNetworking() {
        try {
            ServerSocket server = new ServerSocket(1025);
            File f = new File("storage.set");
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
                // Read objects in the exact order they were written
                Book book = (Book) ois.readObject();
                Movie movie = (Movie) ois.readObject();
                Movie movie2 = (Movie) ois.readObject();
                AudioBooks audiobook = (AudioBooks) ois.readObject();
                Game game = (Game) ois.readObject();
                ss.addBook(book);
                ss.addMovie(movie);
                ss.addMovie(movie2);
                ss.addGame(game);


                // Optionally, output the objects to verify they've been read correctly
                System.out.println("Book: " + book.toString());
                System.out.println("Movie: " + movie.toString());
                System.out.println("Movie 2: " + movie2.toString());
                System.out.println("AudioBook: " + audiobook.toString());
                System.out.println("Game: " + game.toString());


            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.out.println("Class not found exception. Make sure all object classes are available.");
                e.printStackTrace();
            }
            while (true) {
                Socket clientSocket = server.accept();
                System.out.println("incoming transmission");

                sockets.add(clientSocket);

                Thread t = new Thread(new ClientHandler(clientSocket, ss));
                t.start();
            }
        } catch (IOException ioe) {}
    }

    class ClientHandler implements Runnable {

        private Socket clientSocket;
        private serverStorage ss;
        ClientHandler(Socket clientSocket, serverStorage ss) {
            this.clientSocket = clientSocket;
            this.ss = ss;
        }
        public void run() {
            try {
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String message;
                while ((message = reader.readLine()) != null) {
                    System.out.println("RECEIVED: " + message);
                    if(message.equals("book")){
                        sendABook(ss.books.get(0), clientSocket);
                    }
                    System.out.println(clientSocket.getInetAddress());
                    writer.println(message);
                    writer.flush();
                }
                Book book = (Book)(new ObjectInputStream(clientSocket.getInputStream()).readObject());
                System.out.println("GOT THE BOOK " + book.toString());
            } catch (IOException | ClassNotFoundException ioe) {
            }
        }
    }
    public void sendABook(Book book, Socket clientSocket) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
        oos.writeObject(book);
        oos.flush();
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
