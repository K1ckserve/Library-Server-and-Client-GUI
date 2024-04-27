package Serverside;

import java.io.Serializable;

public class Movie implements Serializable {
    private String title;
    private String director;
    public Movie(String title, String director) {
        this.title = title;
        this.director = director;
    }
    @Override
    public String toString() {
        return this.title + " : " + this.director;
    }
}

