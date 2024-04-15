package network;

import java.io.*;

public class Serialization {
    public static void main(String[] args) throws IOException {
//        Book book = new Book("Moby Dick", "Herman Melville", 1, "jfkdj");
//        Movie movie = new Movie("Star Wars: The Empire Rises", "George Lucas");
//        AudioBooks audiobook = new AudioBooks("Harry Potter", "JK Rowling", 3, "fjfj");
//        Game game = new Game("Sekiro", "Activision", "RPG");
//        Movie movie2 = new Movie("Star Wars: The Return of the Jedi", "George Lucas");
//        File f = new File("storage.set");
//        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
//        oos.writeObject(book);
//        oos.writeObject(movie);
//        oos.writeObject(movie2);
//        oos.writeObject(audiobook);
//        oos.writeObject(game);
//        oos.close();
        File f = new File("storage.set");
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            // Read objects in the exact order they were written
            Book book = (Book) ois.readObject();
            Movie movie = (Movie) ois.readObject();
            Movie movie2 = (Movie) ois.readObject();
            AudioBooks audiobook = (AudioBooks) ois.readObject();
            Game game = (Game) ois.readObject();


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



    }
}
