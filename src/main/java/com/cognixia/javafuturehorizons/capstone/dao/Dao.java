package com.cognixia.javafuturehorizons.capstone.dao;

import java.util.Map;
import java.util.Optional;

import com.cognixia.javafuturehorizons.capstone.exception.BookNotFoundException;
import com.cognixia.javafuturehorizons.capstone.exception.UserNotFoundException;
import com.cognixia.javafuturehorizons.capstone.model.Book;
import com.cognixia.javafuturehorizons.capstone.model.User;

import java.sql.SQLException;

/* 
 * Dao interface for the Tracker application.
 * This interface defines the methods for interacting with the database,
 * including user and book management, progress tracking, and rating functionality.
 * 
 * This is concerned with the "how" of data access, not the "what" or "why".
 */
public interface Dao {
  public void establishConnection() throws SQLException;

  public void closeConnection() throws SQLException;

  // create user
  public boolean createUser(User user) throws SQLException;

  // delete user
  public boolean deleteUser(User user) throws SQLException, UserNotFoundException;
  
  // get user by userId
  public Optional<User> getUserById(int userId) throws SQLException, UserNotFoundException;

  // get book by bookId
  public Optional<Book> getBookById(int bookId) throws SQLException, BookNotFoundException;

  // add book to table
  public boolean addBook(Book book) throws SQLException;

  // update book details
  public boolean updateBook(Book book) throws SQLException, BookNotFoundException;

  // delete book
  public boolean deleteBook(Book book) throws SQLException, BookNotFoundException;

  // get all tracker progress of a user
  public Map<Book, Integer> getUserProgress(User user) throws SQLException, UserNotFoundException;

  // get tracker progress of a user for a specific book
  public Optional<Integer> getUserProgress(User user, Book book) throws SQLException, UserNotFoundException;

  // get all users progress of a book
  public Map<User, Integer> getAllUsersProgress(Book book) throws SQLException, BookNotFoundException;

  // update tracker progress
  public boolean updateProgress(User user, Book book, int progress) throws SQLException, UserNotFoundException, BookNotFoundException;

  // rate book
  public boolean rateBook(User user, Book book, int rating) throws SQLException, UserNotFoundException, BookNotFoundException;

  // get average rating of a book
  public Optional<Double> getAverageRating(Book book) throws SQLException, BookNotFoundException;
}
