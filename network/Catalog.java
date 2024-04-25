package network;

import java.io.Serializable;
import java.util.ArrayList;

public class Catalog implements Serializable {
    public ArrayList<Book> books = new ArrayList<>();
    public ArrayList<Movie> movies = new ArrayList<>();
    public ArrayList<AudioBooks> audioBooks = new ArrayList<>();
    public ArrayList<Game> games = new ArrayList<>();
    public synchronized void addBook(Book book) {
        books.add(book);
    }
    public synchronized void addMovie(Movie movie) {
        movies.add(movie);
    }
    public synchronized void addAudioBook(AudioBooks audioBook) {
        audioBooks.add(audioBook);
    }
    public synchronized void addGame(Game game) {
        games.add(game);
    }
    public synchronized void copy(Catalog catalog) {
        books.addAll(catalog.books);
        movies.addAll(catalog.movies);
        audioBooks.addAll(catalog.audioBooks);
        games.addAll(catalog.games);
    }
    public synchronized void removeBook(Book book) {
        books.remove(book);
    }
    public synchronized void removeMovie(Movie movie) {
        movies.remove(movie);
    }
    public synchronized void removeAudioBook(AudioBooks audioBook) {
        audioBooks.remove(audioBook);
    }
    public synchronized void removeGame(Game game) {
        games.remove(game);
    }


}
