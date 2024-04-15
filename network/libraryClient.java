package network;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class libraryClient {
    public static void main(String[] args) {
        new libraryClient().setupNetworking();
    }

    private void setupNetworking() {
        clientStorage cs = new clientStorage();
        try {
            Socket socket = new Socket("192.168.1.200", 1025);
            System.out.println("network established");
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            //BufferedReader reader = new BufferedReader((new InputStreamReader(socket.getInputStream())));
            Thread objReader = new Thread(new serverHandler(socket,cs));
            objReader.start();
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String input = scanner.nextLine();
                writer.println(input);
                writer.flush();
            }

//            Book book = new Book("Moby Dick", "Herman Melville", 1, "jfkdj");
//            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
//            oos.writeObject(book);
//            oos.flush();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    class serverHandler implements Runnable {
        Socket socket;
        clientStorage cs;
        public serverHandler (Socket socket, clientStorage cs) throws IOException {
            this.socket = socket;
            this.cs = cs;
        }
        @Override
        public void run() {
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                while(true){
                    Object recievedObject =  ois.readObject();
                    if(recievedObject != null){
                        if(recievedObject instanceof Book){
                            Book book = (Book) recievedObject;
                            cs.addBook(book);
                            System.out.println(book);
                        }
                        else if (recievedObject instanceof Movie){
                            Movie movie = (Movie) recievedObject;
                            cs.addMovie(movie);
                            System.out.println(movie);
                        }
                        else if (recievedObject instanceof Game){
                            Game game = (Game) recievedObject;
                            cs.addGame(game);
                            System.out.println(game);
                        }
                        else if (recievedObject instanceof AudioBooks){
                            AudioBooks audioBooks = (AudioBooks) recievedObject;
                            cs.addAudioBook(audioBooks);
                            System.out.println(audioBooks);
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
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
//    public Movie recieveAMovie(Socket socket) throws IOException, ClassNotFoundException {
//        Movie movie = (Movie)(new ObjectInputStream(socket.getInputStream()).readObject());
//        return movie;
//    }
//    public Game recieveAGame(Socket socket) throws IOException, ClassNotFoundException {
//        Game game = (Game)(new ObjectInputStream(socket.getInputStream()).readObject());
//        return game;
//    }
//    public AudioBooks recieveAudioBooks(Socket socket) throws IOException, ClassNotFoundException {
//        AudioBooks audioBooks = (AudioBooks)(new ObjectInputStream(socket.getInputStream()).readObject());
//        return audioBooks;
//    }
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
