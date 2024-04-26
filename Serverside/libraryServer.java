package Serverside;

import network.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class libraryServer {

    public static void main(String[] args) {
        new libraryServer().setupNetworking();
    }
    public Map<String, String> userPass = new HashMap<>();
    List<Socket> sockets = new ArrayList<Socket>();
    Catalog ss = new Catalog();
    private Map<user, Catalog> historyMap = new HashMap<>();
    public List<user> users = new ArrayList<>();
    List<ObjectOutputStream> all = new ArrayList<>();

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
                user ben = new user("ben", "123");
                user tan = new user("tan", "456");
                users.add(ben);
                users.add(tan);
                userPass.put("ben", "123");
                for(user u : users){
                    historyMap.put(u, new Catalog());
                }



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
                //BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                all.add(oos);
                boolean loggedIn = false;
                while(!loggedIn) {
                    String username = (String) ois.readObject();
                    String password = (String) ois.readObject();
                    if(!(username.equals("logout") || password.equals("logout"))) {
                        for (user u : users) {
                            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                                oos.writeObject(u);
                                loggedIn = true;
                                oos.writeObject(loggedIn);
                                System.out.println("User " + username + " has been logged in.");
                                ClientHandler clientHandler = new ClientHandler(clientSocket,ss, ois, oos, u);
                                Thread t = new Thread(clientHandler); // Wrap ClientHandler in a Thread and start it
                                sendCatalog(ss, oos);
                                t.start();
                                break;
                            }
                        }
                        oos.writeObject(loggedIn);
                    }else{
                        all.remove(oos);
                        clientSocket.close();
                        break;
                    }
                }
            }
        } catch (IOException ioe) {
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    class ClientHandler implements Runnable {

        private Catalog ss;
        private Socket clientSocket;
        private final ObjectInputStream ois;
        private final ObjectOutputStream oos;
        private final user use;
        private Catalog userCatalog = new Catalog();

        ClientHandler(Socket clientSocket,Catalog ss, ObjectInputStream ois, ObjectOutputStream oos, user use) throws IOException {
            this.clientSocket = clientSocket;
            this.ois = ois;
            this.oos = oos;
            this.ss = ss;
            this.use = use;
            sendUserCatalog(historyMap.get(use), oos);
        }

        public void run() {
            try {
                String datatype;
                while ((datatype = (String) ois.readObject()) != null) {
                    synchronized (this) {
                        Catalog ff = new Catalog();
                        System.out.println(datatype);
                        if (datatype.equals("message")) {
                            String message = (String) ois.readObject();
                            System.out.println("RECEIVED: " + message);
                            if(message.equals("logout")){
                                all.remove(oos);
                                clientSocket.close();
                                break;
                            }
                            if (message.equals("book")) {
                                Iterator<Book> iterator = ss.books.iterator();
                                String message1 = (String) ois.readObject();
                                while (iterator.hasNext()) {
                                    Book b = iterator.next();
                                    if (b.toString().equals(message1)) {
                                        sendAObject(b, oos);
                                        historyMap.get(use).books.add(b);
                                        iterator.remove(); // Remove the current item using iterator
                                        //sendCatalog(ss, oos);
                                        break; // Exit the loop after removing the item
                                    }
                                }
                            } else if (message.equals("movie")) {
                                Iterator<Movie> iterator = ss.movies.iterator();
                                String message1 = (String) ois.readObject();
                                while (iterator.hasNext()) {
                                    Movie m = iterator.next();
                                    if (m.toString().equals(message1)) {
                                        sendAObject(m, oos);
                                        historyMap.get(use).movies.add(m);
                                        iterator.remove();
                                        break;
                                    }
                                }
                            } else if (message.equals("game")) {
                                Iterator<Game> iterator = ss.games.iterator();
                                String message1 = (String) ois.readObject();
                                while (iterator.hasNext()) {
                                    Game g = iterator.next();
                                    if (g.toString().equals(message1)) {
                                        sendAObject(g, oos);
                                        historyMap.get(use).games.add(g);
                                        iterator.remove(); // Remove the current item using iterator
                                        break; // Exit the loop after removing the item
                                    }
                                }
                            } else if (message.equals("audiobook")) {
                                Iterator<AudioBooks> iterator = ss.audioBooks.iterator();
                                String message1 = (String) ois.readObject();
                                while (iterator.hasNext()) {
                                    AudioBooks a = iterator.next();
                                    if (a.toString().equals(message1)) {
                                        sendAObject(a, oos);
                                        historyMap.get(use).audioBooks.add(a);
                                        iterator.remove(); // Remove the current item using iterator
                                        break; // Exit the loop after removing the item
                                    }
                                }
                            }
                            ff.copy(ss);
                            sendCatalog(ff, oos);
                        } else {
                            //ois.reset();
                            System.out.println("made it here");
                            String message = (String) ois.readObject();
                            System.out.println(message);
                            Object recievedObject = ois.readObject();
                            if (recievedObject != null) {
                                if (recievedObject instanceof Book) {
                                    Book book = (Book) recievedObject;
                                    ss.addBook(book);
                                    Iterator<Book> iterator = historyMap.get(use).books.iterator();
                                    while (iterator.hasNext()) {
                                        Book b = iterator.next();
                                        if (b.toString().equals(book.toString())) {
                                            iterator.remove();
                                            break;
                                        }
                                    }
                                    System.out.println(book);
                                } else if (recievedObject instanceof Movie) {
                                    Movie movie = (Movie) recievedObject;
                                    ss.addMovie(movie);
                                    Iterator<Movie> iterator = historyMap.get(use).movies.iterator();
                                    while (iterator.hasNext()) {
                                        Movie m = iterator.next();
                                        if (m.toString().equals(movie.toString())) {
                                            iterator.remove();
                                            break;
                                        }
                                    }
                                    System.out.println(movie);
                                } else if (recievedObject instanceof Game) {
                                    Game game = (Game) recievedObject;
                                    ss.addGame(game);
                                    Iterator<Game> iterator = historyMap.get(use).games.iterator();
                                    while (iterator.hasNext()) {
                                        Game g = iterator.next();
                                        if (g.toString().equals(game.toString())) {
                                            iterator.remove();
                                            break;
                                        }
                                    }
                                    System.out.println(game);
                                } else if (recievedObject instanceof AudioBooks) {
                                    AudioBooks audioBooks = (AudioBooks) recievedObject;
                                    ss.addAudioBook(audioBooks);
                                    Iterator<AudioBooks> iterator = historyMap.get(use).audioBooks.iterator();
                                    while (iterator.hasNext()) {
                                        AudioBooks a = iterator.next();
                                        if (a.toString().equals(audioBooks.toString())) {
                                            iterator.remove();
                                            break;
                                        }
                                    }
                                    System.out.println(audioBooks);
                                }
                            }
                            ff.copy(ss);
                            sendCatalog(ff, oos);
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendCatalog(Catalog c, ObjectOutputStream oos) throws IOException {
        for(ObjectOutputStream sen : all){
            sen.writeObject(c);
            sen.flush();
            sen.writeObject("server");
            sen.flush();
        }
    }
    public void sendUserCatalog(Catalog c, ObjectOutputStream oos) throws IOException {
        oos.writeObject(c);
        oos.flush();
        oos.writeObject("user");
        oos.flush();
    }

    public void sendAObject(Object book, ObjectOutputStream oos) throws IOException {
        oos.writeObject(book);
        oos.flush();
    }
}