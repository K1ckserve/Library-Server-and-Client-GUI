package Serverside;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import common.*;

public class Server {

    public static void main(String[] args) {
        new Server().setupNetworking();
    }
    List<Socket> sockets = new ArrayList<Socket>();
    Catalog ss = new Catalog();
    private Map<User, Catalog> historyMap = new HashMap<>();
    public List<User> users = new ArrayList<>();
    public List<ObjectOutputStream> all = new ArrayList<>();
    ObjectOutputStream fileOut;
    ObjectInputStream fileIn;
    Catalog unchangingCatalog = new Catalog();

    public void setupNetworking() {
        try {
            ServerSocket server = new ServerSocket(3578);
            Serialization serialization = new Serialization();
            fileIn = serialization.ois;
            fileOut = serialization.initialize();
            deserializeObjects();
            // Read objects in the exact order they were written
            for (User u : users) {
                historyMap.put(u, new Catalog());
            }
            // Optionally, output the objects to verify they've been read correctly
            while (true) {
                Socket clientSocket = server.accept();
                System.out.println("incoming transmission");

                sockets.add(clientSocket);
                //BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                Thread m = new Thread(new loginHandler(ois, oos, clientSocket));
                //all.add(oos);
                m.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public void createUser(String username, String password) throws IOException {
        User newUser = new User(username, password);
        users.add(newUser);
        fileOut.reset();
        for(Book b : unchangingCatalog.books) {
            fileOut.writeObject(b);
        }
        for(Movie m : unchangingCatalog.movies) {
            fileOut.writeObject(m);
        }
        for(Game g : unchangingCatalog.games) {
            fileOut.writeObject(g);
        }
        for(AudioBook a : unchangingCatalog.audioBooks) {
            fileOut.writeObject(a);
        }
        for(User user : users) { //dont do this yet
            fileOut.writeObject(user); // Write each user object
        }
    }
    public void serilizeResetPass(String username, String password) throws IOException {
        for (User u1 : users) {
            if(u1.getUsername().equals(username)) {
                u1.setPassword(password);
            }
        }
        fileOut.reset();
        for(Book b : unchangingCatalog.books) {
            fileOut.writeObject(b);
        }
        for(Movie m : unchangingCatalog.movies) {
            fileOut.writeObject(m);
        }
        for(Game g : unchangingCatalog.games) {
            fileOut.writeObject(g);
        }
        for(AudioBook a : unchangingCatalog.audioBooks) {
            fileOut.writeObject(a);
        }
        for(User user : users) { //dont do this yet
            fileOut.writeObject(user); // Write each user object
        }
    }
    private void deserializeObjects() throws IOException, ClassNotFoundException {
        while (true) {
            try {
                Object obj = fileIn.readObject();
                if (obj instanceof Book) {
                    Book book = (Book) obj;
                    unchangingCatalog.addBook(book);
                    ss.addBook(book);
                    System.out.println(book);
                } else if (obj instanceof Movie) {
                    Movie movie = (Movie) obj;
                    unchangingCatalog.addMovie(movie);
                    ss.addMovie(movie);
                    System.out.println(movie);
                } else if (obj instanceof AudioBook) {
                    AudioBook audiobook = (AudioBook) obj;
                    unchangingCatalog.addAudioBook(audiobook);
                    ss.addAudioBook(audiobook);
                    System.out.println(audiobook);
                } else if (obj instanceof Game) {
                    Game game = (Game) obj;
                    unchangingCatalog.addGame(game);
                    ss.addGame(game);
                    System.out.println(game);
                } else if (obj instanceof User) {
                    User user = (User) obj;
                    users.add(user);
                    System.out.println(user);
                }
            }catch(EOFException f){
                break;
            }
        }
    }

    class ClientHandler implements Runnable {

        private Catalog ss;
        private Socket clientSocket;
        private final ObjectInputStream ois;
        private final ObjectOutputStream oos;
        private final User use;
        private Catalog userCatalog = new Catalog();

        ClientHandler(Socket clientSocket, Catalog ss, ObjectInputStream ois, ObjectOutputStream oos, User use) throws IOException {
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
                            if (message.equals("logout")) {
                                oos.writeObject("die");
                                all.remove(oos);
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
                                Iterator<AudioBook> iterator = ss.audioBooks.iterator();
                                String message1 = (String) ois.readObject();
                                while (iterator.hasNext()) {
                                    AudioBook a = iterator.next();
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
                                } else if (recievedObject instanceof AudioBook) {
                                    AudioBook audioBooks = (AudioBook) recievedObject;
                                    ss.addAudioBook(audioBooks);
                                    Iterator<AudioBook> iterator = historyMap.get(use).audioBooks.iterator();
                                    while (iterator.hasNext()) {
                                        AudioBook a = iterator.next();
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
    class loginHandler implements Runnable {
        public ObjectInputStream ois;
        public ObjectOutputStream oos;
        public Socket clientSocket;
        public loginHandler (ObjectInputStream ois, ObjectOutputStream oos, Socket clientSocket){
            this.ois = ois;
            this.oos = oos;
            this.clientSocket = clientSocket;
        }
        @Override
        public void run(){
            try{
            boolean loggedIn = false;
            boolean reset= false;
            while(!loggedIn) {
                String action = (String) ois.readObject();
                String username = (String) ois.readObject();
                String password = (String) ois.readObject();
                if (action.equals("Reset")) {
                    serilizeResetPass(username, password);
                } else if (action.equals("Login")) {
                    for (User u : users) {
                        if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                            oos.reset();
                            oos.writeObject("success");
                            oos.writeObject(u);
                            loggedIn = true;
                            oos.writeObject(loggedIn);
                            System.out.println("User " + username + " has been logged in.");
                            ClientHandler clientHandler = new ClientHandler(clientSocket, ss, ois, oos, u);
                            Thread t = new Thread(clientHandler); // Wrap ClientHandler in a Thread and start it
                            t.start();
                            all.add(oos);
                            sendCatalog(ss, oos);
                            t.join();
                            loggedIn=false;
                            reset=true;
                            break;
                        }
                    }
                    if(!reset) {
                        oos.writeObject("failure");
                        oos.writeObject(loggedIn);
                    }
                    reset=false;
                }else if(action.equals("New")) {
                    createUser(username, password);
                }
            }
        }catch(IOException e) {
                throw new RuntimeException(e);
            }
            catch(ClassNotFoundException m){
                throw new RuntimeException(m);
            } catch (InterruptedException e) {
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