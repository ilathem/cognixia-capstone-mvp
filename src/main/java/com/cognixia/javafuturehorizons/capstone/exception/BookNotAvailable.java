package com.cognixia.javafuturehorizons.capstone.exception;

public class BookNotAvailable extends Exception {

  public BookNotAvailable(String title) {
    super("The book '" + title + "' is not available to track.");
  }

}
