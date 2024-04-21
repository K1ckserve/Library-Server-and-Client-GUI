package network;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class libraryClient {
    private PrintWriter writer;
    public void sendLoginCredentials(String username, String password) {
        writer.println(username);
        writer.println(password);
        writer.flush();
    }
    public static void main(String[] args) {
        new libraryClient().setupNetworking();
    }

    private void setupNetworking() {
        clientStorage cs = new clientStorage();
        catalog c = new catalog();
        try {
            Socket socket = new Socket("192.168.1.151", 1025);
            System.out.println("network established");
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            //BufferedReader reader = new BufferedReader((new InputStreamReader(socket.getInputStream())));
            Thread objReader = new Thread(new reciever (socket,cs, c));
            objReader.start();
            Scanner scanner = new Scanner(System.in);
//            System.out.println("username");
//            if(scanner.hasNextLine()) {
//                writer.println(scanner.nextLine());
//                writer.flush();
//                System.out.println("password");
//                writer.println(scanner.nextLine());
//                writer.flush();
//            }
            while (true) {
                String input = scanner.nextLine();
                if(input.equals("send")){
                    String specific = scanner.nextLine();
                    if(specific.equals("book")){
                        if(!cs.books.isEmpty()){
                            writer.println("object");
                            writer.flush();
                            sendABook(cs.books.get(0), socket);
                        }
                    }
                    else if(specific.equals("movie")){
                        if(!cs.movies.isEmpty()){
                            writer.println("object");
                            writer.flush();
                            sendAMovie(cs.movies.get(0), socket);
                        }
                    }
                    else if(specific.equals("game")){
                        if(!cs.games.isEmpty()){
                            writer.println("object");
                            writer.flush();
                            sendAGame(cs.games.get(0), socket);
                        }
                    }
                    else if(specific.equals("audiobooks")){
                        if(!cs.audioBooks.isEmpty()){
                            writer.println("object");
                            writer.flush();
                            sendAAudioBook(cs.audioBooks.get(0), socket);
                        }
                    }
                }else {
                    writer.println("message");
                    writer.flush();
                    writer.println(input);
                    writer.flush();
                }
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    class reciever implements Runnable {
        Socket socket;
        clientStorage cs;
        catalog cat;
        public reciever (Socket socket, clientStorage cs, catalog cat) throws IOException {
            this.socket = socket;
            this.cs = cs;
            this.cat = cat;
        }
        @Override
        public void run() {
            try {
                //ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                while(true){
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    Object recievedObject =  ois.readObject();
                    if(recievedObject != null){
                        if(recievedObject instanceof catalog){
                            cat = (catalog) recievedObject;
                        }
                        else if(recievedObject instanceof Book){
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
                    //ois.mark(1024);
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
