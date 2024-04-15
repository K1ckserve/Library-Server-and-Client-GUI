package network;

import java.util.ArrayList;

public class serverStorage {
    public ArrayList<Book> books = new ArrayList<>();
    public ArrayList<Movie> movies = new ArrayList<>();
    public ArrayList<AudioBooks> audioBooks = new ArrayList<>();
    public ArrayList<Game> games = new ArrayList<>();
    public void addBook(Book book) {
        books.add(book);
    }
    public void addMovie(Movie movie) {
        movies.add(movie);
    }
    public void addAudioBook(AudioBooks audioBook) {
        audioBooks.add(audioBook);
    }
    public void addGame(Game game) {
        games.add(game);
    }
    public void removeBook(Book book) {
        books.remove(book);
    }
    public void removeMovie(Movie movie) {
        movies.remove(movie);
    }
    public void removeAudioBook(AudioBooks audioBook) {
        audioBooks.remove(audioBook);
    }
    public void removeGame(Game game) {
        games.remove(game);
    }


}
