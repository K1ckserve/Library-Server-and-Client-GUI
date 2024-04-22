package network;

import java.io.Serializable;

public class Game implements Serializable {
    private String title;
    private String creator;
    private String genre;
    public Game(String title, String creator, String genrep) {
        this.title = title;
        this.creator = creator;
        this.genre = genre;
    }
    @Override
    public String toString() {
        return this.title + " : " + this.creator;
    }

}
