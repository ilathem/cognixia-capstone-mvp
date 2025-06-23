package com.cognixia.javafuturehorizons.capstone.exception;

public class BookNotFoundException extends Exception {

  public BookNotFoundException(String title) {
    super("The book '" + title + "' was not found.");
  }

}
