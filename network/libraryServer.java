package network;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class libraryServer {
    private ArrayList<user> history = new ArrayList<>();
    public Map<String, String> userPass = new HashMap<>();
    private List<ObjectOutputStream> updates = new ArrayList<>();
    public static void main(String[] args) {
        new libraryServer().setupNetworking();
    }

    List<Socket> sockets = new ArrayList<Socket>();
    Catalog ss = new Catalog();

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
                user demo = new user("ben", "123");
                userPass.put("ben", "123");


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
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                String username = reader.readLine();
                String password = reader.readLine();
                if(userPass.containsKey(username)) {
                    if(userPass.get(username).equals(password)) {
                        System.out.println("User " + username + " has been logged in.");
                        ClientHandler clientHandler = new ClientHandler(clientSocket, ss, reader, ois, oos);
                        Thread t = new Thread(clientHandler); // Wrap ClientHandler in a Thread and start it
                        sendCatalog(ss, oos);
//                        Thread s = new Thread(new CatalogSender(oos));
//                        s.start();
                        t.start();
                    }
                }
            }
        } catch (IOException ioe) {}
    }

    class ClientHandler implements Runnable {

        private final Socket clientSocket;
        private Catalog ss;
        private final ObjectInputStream ois;
        private final ObjectOutputStream oos;
        private final BufferedReader reader;
        private Catalog ff = new Catalog();

        ClientHandler(Socket clientSocket, Catalog ss, BufferedReader r, ObjectInputStream ois, ObjectOutputStream oos) throws IOException {
            this.ois= ois;
            this.oos = oos;
            this.clientSocket = clientSocket;
            this.ss = ss;
            this.reader = r;
        }

        public void run() {
            try {
                String datatype;
                System.out.println("make it here");
                while ((datatype = reader.readLine()) != null) {
                    synchronized (this){
                    if (datatype.equals("message")) {
                        String message = reader.readLine();
                        System.out.println("RECEIVED: " + message);
                        if (message.equals("book")) {
                            Iterator<Book> iterator = ss.books.iterator();
                            String message1 = reader.readLine();
                            while (iterator.hasNext()) {
                            Book b = iterator.next();
                            if (b.toString().equals(message1)) {
                                sendABook(b, oos);
                                iterator.remove(); // Remove the current item using iterator
                                //sendCatalog(ss, oos);
                                break; // Exit the loop after removing the item
                            }
                        }
                    } else if (message.equals("movie")) {
                        Iterator<Movie> iterator = ss.movies.iterator();
                        String message1 = reader.readLine();
                        while (iterator.hasNext()) {
                            Movie m = iterator.next();
                            if (m.toString().equals(message1)) {
                                sendAMovie(m, oos);
                                iterator.remove();
                                break;
                            }
                        }
                    } else if (message.equals("game")) {
                        Iterator<Game> iterator = ss.games.iterator();
                        String message1 = reader.readLine();
                        while (iterator.hasNext()) {
                            Game g = iterator.next();
                            if (g.toString().equals(message1)) {
                                sendAGame(g, oos);
                                iterator.remove(); // Remove the current item using iterator
                                break; // Exit the loop after removing the item
                            }
                        }
                    } else if (message.equals("audiobook")) {
                            Iterator<AudioBooks> iterator = ss.audioBooks.iterator();
                            String message1 = reader.readLine();
                            while (iterator.hasNext()) {
                                AudioBooks a = iterator.next();
                                if (a.toString().equals(message1)) {
                                    sendAAudioBook(a, oos);
                                    iterator.remove(); // Remove the current item using iterator
                                    break; // Exit the loop after removing the item
                                }
                            }
                        }
                        ff.copy(ss);
                        sendCatalog(ff, oos);
                    } else {
                        Object recievedObject = ois.readObject();
                        if (recievedObject != null) {
                            if (recievedObject instanceof Book) {
                                System.out.println("made it here");
                                Book book = (Book) recievedObject;
                                ss.addBook(book);
                                System.out.println(book);
                            } else if (recievedObject instanceof Movie) {
                                Movie movie = (Movie) recievedObject;
                                ss.addMovie(movie);
                                System.out.println(movie);
                            } else if (recievedObject instanceof Game) {
                                System.out.println("made it here");
                                Game game = (Game) recievedObject;
                                ss.addGame(game);
                                System.out.println(game);
                            } else if (recievedObject instanceof AudioBooks) {
                                AudioBooks audioBooks = (AudioBooks) recievedObject;
                                ss.addAudioBook(audioBooks);
                                System.out.println(audioBooks);
                            }
                        }
                        ff.copy(ss);
                        sendCatalog(ff, oos);
                    }
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void sendCatalog(Catalog c, ObjectOutputStream oos) throws IOException {
        oos.writeObject(c);
        oos.flush();
    }
    class CatalogSender implements Runnable {
        public ObjectOutputStream oos;
        public CatalogSender(ObjectOutputStream oos){
            this.oos = oos;
        }
        public void run() {
            try {
                while(true) {
                    Thread.sleep(1000); // Adjust the interval as needed (currently every 5 seconds)
                    oos.writeObject(ss);
                    oos.flush();
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void sendABook(Book book, ObjectOutputStream oos) throws IOException {
        oos.writeObject(book);
        oos.flush();
    }
    public void sendAMovie(Movie movie, ObjectOutputStream oos) throws IOException {
        oos.writeObject(movie);
        oos.flush();
    }
    public void sendAGame(Game game, ObjectOutputStream oos) throws IOException {
        oos.writeObject(game);
        oos.flush();
    }
    public void sendAAudioBook(AudioBooks audioBooks, ObjectOutputStream oos) throws IOException {
        oos.writeObject(audioBooks);
        oos.flush();
    }
}

