package com.cognixia.javafuturehorizons.capstone.model;

public class Tracker {
    private Book book;
    private int progress;

    public Tracker() {
    }

    public Tracker(Book book, int progress) {
        this.book = book;
        this.progress = progress;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    @Override
    public String toString() {
        return "Tracker{" +
                "book=" + book +
                ", progress=" + progress +
                '}';
    }
}
