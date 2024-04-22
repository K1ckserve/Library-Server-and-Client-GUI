package network;

import java.io.Serializable;

public class AudioBooks implements Serializable {

    private String title;
    private String author;
    private int pages;
    private String summary;

    public AudioBooks(String title, String author, int pages, String summary) {
        this.title = title;
        this.author = author;
        this.pages = pages;
        this.summary = summary;
    }

    @Override
    public String toString() {
        return this.title + " : " + this.author;
    }
}
