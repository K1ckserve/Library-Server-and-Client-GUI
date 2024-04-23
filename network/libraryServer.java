package network;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class libraryServer {
    private ArrayList<user> history = new ArrayList<>();
    public Map<String, String> userPass = new HashMap<>();
    public static void main(String[] args) {
        new libraryServer().setupNetworking();
    }

    List<Socket> sockets = new ArrayList<Socket>();
    Catalog ss = new Catalog();

    private void setupNetworking() {
        try {
            ServerSocket server = new ServerSocket(1025);
            Thread s = new Thread(new CatalogSender());
            s.start();
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
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String username = reader.readLine();
                String password = reader.readLine();
                if(userPass.containsKey(username)) {
                    if(userPass.get(username).equals(password)) {
                        System.out.println("User " + username + " has been logged in.");
                        Thread t = new Thread(new ClientHandler(clientSocket, ss));
                        t.start();
                    }
//                    }
                }
            }
        } catch (IOException ioe) {}
    }

    class ClientHandler implements Runnable {

        private Socket clientSocket;
        private Catalog ss;

        ClientHandler(Socket clientSocket, Catalog ss) {
            this.clientSocket = clientSocket;
            this.ss = ss;
        }

        public void run() {
            try {
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String datatype;
                while ((datatype = reader.readLine()) != null) {
                    synchronized (this){
                    if (datatype.equals("message")) {
                        String message = reader.readLine();
                        System.out.println("RECEIVED: " + message);
                        if (message.equals("book")) {
                            for(Book b : ss.books){
                                String message1 = reader.readLine();
                                if(b.toString().equals(message1)){
                                    sendABook(b, clientSocket);
                                    ss.removeBook(b);
                                }
                            }
                        } else if (message.equals("movie")) {
                            for(Movie m : ss.movies){
                                String message1 = reader.readLine();
                                if(m.toString().equals(message1)){
                                    sendAMovie(m, clientSocket);
                                    ss.removeMovie(m);
                                }
                            }
                        } else if (message.equals("game")) {
                            for(Game g : ss.games){
                                String message1 = reader.readLine();
                                if(g.toString().equals(message1)){
                                    sendAGame(g, clientSocket);
                                    ss.removeGame(g);
                                }
                            }
                        } else if (message.equals("audiobook")) {
                            for(AudioBooks a : ss.audioBooks){
                                String message1 = reader.readLine();
                                if(a.toString().equals(message1)){
                                    sendAAudioBook(a, clientSocket);
                                    ss.removeAudioBook(a);
                                }
                            }
                        }
                    } else {
                        ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                        Object recievedObject = ois.readObject();
                        if (recievedObject != null) {
                            if (recievedObject instanceof Book) {
                                Book book = (Book) recievedObject;
                                ss.addBook(book);
                                System.out.println(book);
                            } else if (recievedObject instanceof Movie) {
                                Movie movie = (Movie) recievedObject;
                                ss.addMovie(movie);
                                System.out.println(movie);
                            } else if (recievedObject instanceof Game) {
                                Game game = (Game) recievedObject;
                                ss.addGame(game);
                                System.out.println(game);
                            } else if (recievedObject instanceof AudioBooks) {
                                AudioBooks audioBooks = (AudioBooks) recievedObject;
                                ss.addAudioBook(audioBooks);
                                System.out.println(audioBooks);
                            }
                        }
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
    class CatalogSender implements Runnable {
        public void run() {
            try {
                while(true) {
                    Thread.sleep(100); // Adjust the interval as needed (currently every 5 seconds)
                    sendCatalogToAllClients();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void sendCatalogToAllClients() {
            try {
                for (Socket socket : sockets) {
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(ss);
                    oos.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
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

