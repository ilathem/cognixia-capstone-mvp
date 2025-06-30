package com.cognixia.javafuturehorizons.capstone.dao;

import java.util.List;
import java.util.Optional;

import com.cognixia.javafuturehorizons.capstone.exception.BookNotFoundException;
import com.cognixia.javafuturehorizons.capstone.exception.UserNotFoundException;
import com.cognixia.javafuturehorizons.capstone.model.Book;
import com.cognixia.javafuturehorizons.capstone.model.Tracker;
import com.cognixia.javafuturehorizons.capstone.model.User;
import com.cognixia.javafuturehorizons.capstone.model.UserProgress;

import java.sql.SQLException;

/* 
 * Dao interface for the Tracker application.
 * This interface defines the methods for interacting with the database,
 * including user and book management, progress tracking, and rating functionality.
 * 
 * This is concerned with the "how" of data access, not the "what" or "why".
 */
public interface Dao {
    // create user
    public User createUser(User user) throws SQLException, ClassNotFoundException;

    // delete user
    public boolean deleteUser(User user) throws SQLException, UserNotFoundException, ClassNotFoundException;

    // get user by userId
    public Optional<User> getUserById(int userId) throws SQLException, UserNotFoundException, ClassNotFoundException;

    // validate user credentials
    public Optional<User> validateUser(String username, String password) throws SQLException, ClassNotFoundException;

    // get book by bookId
    public Optional<Book> getBookById(int bookId) throws SQLException, BookNotFoundException, ClassNotFoundException;

    // add book to table
    public boolean addBook(User user, Book book) throws SQLException, ClassNotFoundException, UserNotFoundException;

    public List<Book> getAllBooks() throws SQLException, ClassNotFoundException;

    // update book details
    public boolean updateBook(User user, Book book) throws SQLException, BookNotFoundException, ClassNotFoundException, UserNotFoundException;

    // delete book
    public boolean deleteBook(User user, Book book) throws SQLException, BookNotFoundException, ClassNotFoundException, UserNotFoundException;

    // get all tracker progress of a user
    public List<Tracker> getUserProgress(User user) throws SQLException, UserNotFoundException, ClassNotFoundException;

    // get tracker progress of a user for a specific book
    public Optional<Tracker> getUserProgress(User user, Book book)
            throws SQLException, UserNotFoundException, ClassNotFoundException;

    // get all users progress of a book
    public List<UserProgress> getAllUsersProgress(Book book)
            throws SQLException, BookNotFoundException, ClassNotFoundException;

    // update tracker progress
    public boolean updateProgress(User user, Book book, int progress) throws SQLException, ClassNotFoundException;

    // rate book
    public boolean rateBook(User user, Book book, int rating) throws SQLException, ClassNotFoundException;

    // get user rating for a book
    public Optional<Integer> getUserRating(User user, Book book)
            throws SQLException, UserNotFoundException, BookNotFoundException, ClassNotFoundException;

    // get average rating of a book
    public Optional<Double> getAverageRating(Book book)
            throws SQLException, BookNotFoundException, ClassNotFoundException;
}
