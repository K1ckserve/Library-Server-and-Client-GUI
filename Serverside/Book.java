package Serverside;

import java.io.Serializable;

public class Book implements Serializable {
    private String title;
    private String author;
    private int pages;
    private String summary;

    public Book(String title, String author, int pages, String summary) {
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
