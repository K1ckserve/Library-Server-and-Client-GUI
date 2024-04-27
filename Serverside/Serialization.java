package Serverside;

import java.io.*;
import common.*;

public class Serialization {
    public static void main(String[] args) throws IOException {
        Book book = new Book("Moby Dick", "Herman Melville", 1, "jfkdj");
        Movie movie = new Movie("Star Wars: The Empire Rises", "George Lucas");
        AudioBook audiobook = new AudioBook("Harry Potter", "JK Rowling", 3, "fjfj");
        Game game = new Game("Sekiro", "Activision", "RPG");
        Movie movie2 = new Movie("Star Wars: The Return of the Jedi", "George Lucas");
        File f = new File("storage.set");
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
        oos.writeObject(book);
        oos.writeObject(movie);
        oos.writeObject(movie2);
        oos.writeObject(audiobook);
        oos.writeObject(game);
        oos.close();

    }
}
