package com.cognixia.javafuturehorizons.capstone.model;

public class Book {
  private int bookId;
  private String title;
  private String author;
  private int numPages;

  public Book(int bookId, String title, String author, int numPages) {
    this.bookId = bookId;
    this.title = title;
    this.author = author;
    this.numPages = numPages;
  }

  public int getBookId() {
    return bookId;
  }

  public void setBookId(int bookId) {
    this.bookId = bookId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public int getNumPages() {
    return numPages;
  }

  public void setNumPages(int numPages) {
    this.numPages = numPages;
  }

  @Override
  public String toString() {
    return "Book{" +
        "bookId=" + bookId +
        ", title='" + title + '\'' +
        ", author='" + author + '\'' +
        ", numPages=" + numPages +
        '}';
  }
}
