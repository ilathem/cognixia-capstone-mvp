package com.cognixia.javafuturehorizons.capstone.model;

public class Tracker {
    private Book book;
    private int progress;
    private int rating;

    public Tracker() {
    }

    public Tracker(Book book, int progress, int rating) {
        this.book = book;
        this.progress = progress;
        this.rating = rating;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Tracker{" +
                "book=" + book +
                ", progress=" + progress +
                ", rating=" + rating +
                '}';
    }
}
