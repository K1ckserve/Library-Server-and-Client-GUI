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
                ss.addAudioBook(audiobook);


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
                        ss.removeBook(ss.books.get(0));
                    }else if(message.equals("movie")) {
                        sendAMovie(ss.movies.get(0), clientSocket);
                        ss.removeMovie(ss.movies.get(0));
                    } else if(message.equals("game")) {
                        sendAGame(ss.games.get(0), clientSocket);
                        ss.removeGame(ss.games.get(0));
                    }
                    else if (message.equals("audiobook")) {
                        sendAAudioBook(ss.audioBooks.get(0), clientSocket);
                        ss.removeAudioBook(ss.audioBooks.get(0));
                    }
//                    System.out.println(clientSocket.getInetAddress());
//                    writer.println(message);
//                    writer.flush();
                }
//                Book book = (Book)(new ObjectInputStream(clientSocket.getInputStream()).readObject());
//                System.out.println("GOT THE BOOK " + book.toString());
            } catch (IOException ioe) {
            }
        }
    }
    public void sendABook(Book book, Socket clientSocket) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
        oos.writeObject(book);
        oos.flush();
    }
    public void sendAMovie(Movie movie, Socket clientSocket) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
        oos.writeObject(movie);
        oos.flush();
    }
    public void sendAGame(Game game, Socket clientSocket) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
        oos.writeObject(game);
        oos.flush();
    }
    public void sendAAudioBook(AudioBooks audioBooks, Socket clientSocket) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
        oos.writeObject(audioBooks);
        oos.flush();
    }
    public Book recieveABook(Socket socket) throws IOException, ClassNotFoundException {
        Book book = (Book)(new ObjectInputStream(socket.getInputStream()).readObject());
        return book;
    }
    public Movie recieveAMovie(Socket socket) throws IOException, ClassNotFoundException {
        Movie movie = (Movie)(new ObjectInputStream(socket.getInputStream()).readObject());
        return movie;
    }
    public Game recieveAGame(Socket socket) throws IOException, ClassNotFoundException {
        Game game = (Game)(new ObjectInputStream(socket.getInputStream()).readObject());
        return game;
    }
    public AudioBooks recieveAudioBooks(Socket socket) throws IOException, ClassNotFoundException {
        AudioBooks audioBooks = (AudioBooks)(new ObjectInputStream(socket.getInputStream()).readObject());
        return audioBooks;
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
class AudioBooks implements Serializable{

    private String title;
    private String author;
    private int pages;
    private String summary;

    public AudioBooks(String title, String author, int pages, String summary) {
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
