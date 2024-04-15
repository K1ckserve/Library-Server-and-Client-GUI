package network;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class libraryClient {
    public static void main(String[] args) {
        new libraryClient().setupNetworking();
    }

    private void setupNetworking() {
        try {
            Socket socket = new Socket("192.168.1.200", 1025);
            System.out.println("network established");

            Book book = new Book("Moby Dick", "Herman Melville", 1, "jfkdj");
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(book);
            oos.flush();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
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
