package network;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class libraryClient {
    public static void main(String[] args) {
        new libraryClient().setupNetworking();
    }

    private void setupNetworking() {
        try {
            Socket socket = new Socket("192.168.1.200", 1025);
            System.out.println("network established");
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            BufferedReader reader = new BufferedReader((new InputStreamReader(socket.getInputStream())));

            Scanner scanner = new Scanner(System.in);
            while (true) {
                String input = scanner.nextLine();
                writer.println(input);
                writer.flush();

                //String received = reader.readLine();
                //System.out.println("I RECEIVED BACK: " + received);
                Book book = recieveABook(socket);
                System.out.println(book.toString());
            }

//            Book book = new Book("Moby Dick", "Herman Melville", 1, "jfkdj");
//            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
//            oos.writeObject(book);
//            oos.flush();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendAObject(Object obj){

    }
    public Book recieveABook(Socket socket) throws IOException, ClassNotFoundException {
        Book book = (Book)(new ObjectInputStream(socket.getInputStream()).readObject());
        return book;
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
