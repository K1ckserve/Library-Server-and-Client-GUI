package network;

import java.io.*;
import java.net.Socket;

public class libraryClient {
    private Socket socket;
    private PrintWriter writer;
    private clientStorage cs = new clientStorage();
    private Catalog catalog = new Catalog();
    private CatalogUpdateListener listener;

    //Socket socket = new Socket("192.168.1.151", 1025);

    public libraryClient() throws IOException {
    }
    public void setCatalogUpdateListener(CatalogUpdateListener listener) {
        this.listener = listener;
    }
    private void notifyCatalogUpdate() {
        if(listener != null) {
            listener.onCatalogUpdate(catalog);
        }
    }
    private void updateCatalog(){
        notifyCatalogUpdate();
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
            Thread objReader = new Thread(new reciever (socket,cs,catalog));
            objReader.start();
//            Scanner scanner = new Scanner(System.in);
//            while (true) {
//                String input = scanner.nextLine();
//                if(input.equals("send")){
//                    String specific = scanner.nextLine();
//                    if(specific.equals("book")){
//                        if(!cs.books.isEmpty()){
//                            writer.println("object");
//                            writer.flush();
//                            sendABook(cs.books.get(0), socket);
//                        }
//                    }
//                    else if(specific.equals("movie")){
//                        if(!cs.movies.isEmpty()){
//                            writer.println("object");
//                            writer.flush();
//                            sendAMovie(cs.movies.get(0), socket);
//                        }
//                    }
//                    else if(specific.equals("game")){
//                        if(!cs.games.isEmpty()){
//                            writer.println("object");
//                            writer.flush();
//                            sendAGame(cs.games.get(0), socket);
//                        }
//                    }
//                    else if(specific.equals("audiobooks")){
//                        if(!cs.audioBooks.isEmpty()){
//                            writer.println("object");
//                            writer.flush();
//                            sendAAudioBook(cs.audioBooks.get(0), socket);
//                        }
//                    }
//                }else {
//                    writer.println("message");
//                    writer.flush();
//                    writer.println(input);
//                    writer.flush();
//                }
//            }

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
    class reciever implements Runnable {
        Socket socket;
        clientStorage cs;
        Catalog cat;
        public reciever (Socket socket, clientStorage cs, Catalog cat) throws IOException {
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
                        if(recievedObject instanceof Catalog){
                            cat = (Catalog) recievedObject;
                            updateCatalog(cat);
                            libraryClient.this.updateCatalog(cat);
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
