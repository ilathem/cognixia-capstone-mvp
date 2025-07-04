package com.cognixia.javafuturehorizons.capstone.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.cognixia.javafuturehorizons.capstone.connection.ConnectionManager;
import com.cognixia.javafuturehorizons.capstone.exception.BookNotFoundException;
import com.cognixia.javafuturehorizons.capstone.exception.UserNotFoundException;
import com.cognixia.javafuturehorizons.capstone.model.Book;
import com.cognixia.javafuturehorizons.capstone.model.Tracker;
import com.cognixia.javafuturehorizons.capstone.model.User;
import com.cognixia.javafuturehorizons.capstone.model.UserProgress;

public class DaoImpl implements Dao {

  private static DaoImpl instance;

  private DaoImpl() {
  }

  public static DaoImpl getInstance() {
    if (instance == null) {
      instance = new DaoImpl();
    }
    return instance;
  }

  @Override
  public User createUser(User user) throws SQLException, ClassNotFoundException {
    String sql = "INSERT INTO users (name, password) VALUES (?, ?)";
    try (PreparedStatement stmt = ConnectionManager.getConnection().prepareStatement(sql)) {
      stmt.setString(1, user.getName());
      stmt.setString(2, user.getPassword());
      stmt.executeUpdate();
      return this.validateUser(user.getName(), user.getPassword()).get();
    }
  }

  @Override
  public boolean deleteUser(User user) throws SQLException, UserNotFoundException, ClassNotFoundException {
    String sql = "DELETE FROM users WHERE name = ?";
    try (PreparedStatement stmt = ConnectionManager.getConnection().prepareStatement(sql)) {
      stmt.setString(1, user.getName());
      int rowsAffected = stmt.executeUpdate();
      return rowsAffected > 0;
    }
  }

  @Override
  public Optional<User> getUserById(int userId) throws SQLException, UserNotFoundException, ClassNotFoundException {
    String sql = "SELECT * FROM users WHERE user_id = ?";
    try (PreparedStatement stmt = ConnectionManager.getConnection().prepareStatement(sql)) {
      stmt.setInt(1, userId);
      var resultSet = stmt.executeQuery();
      if (resultSet.next()) {
        User user = new User(
            resultSet.getInt("user_id"),
            resultSet.getInt("clearance"),
            resultSet.getString("name"),
            resultSet.getString("password"));
        return Optional.of(user);
      } else {
        throw new UserNotFoundException("User with ID " + userId + " not found.");
      }
    }
  }

  @Override
  public Optional<User> validateUser(String username, String password) throws SQLException, ClassNotFoundException {
    String sql = "SELECT * FROM users WHERE name = ? AND password = ?";
    try (PreparedStatement stmt = ConnectionManager.getConnection().prepareStatement(sql)) {
      stmt.setString(1, username);
      stmt.setString(2, password);
      var resultSet = stmt.executeQuery();
      if (resultSet.next()) {
        User user = new User(
            resultSet.getInt("user_id"),
            resultSet.getInt("clearance"),
            resultSet.getString("name"),
            resultSet.getString("password"));
        return Optional.of(user);
      }
      return Optional.empty();
    }
  }

  @Override
  public List<Book> getAllBooks() throws SQLException, ClassNotFoundException {
    List<Book> books = new ArrayList<>();
    String sql = "SELECT * FROM books";
    try (PreparedStatement stmt = ConnectionManager.getConnection().prepareStatement(sql)) {
      var resultSet = stmt.executeQuery();
      while (resultSet.next()) {
        Book book = new Book(
            resultSet.getInt("book_id"),
            resultSet.getString("title"),
            resultSet.getString("author"),
            resultSet.getInt("num_pages"));
        books.add(book);
      }
      return books;
    }
  }

  @Override
  public Optional<Book> getBookById(int bookId) throws SQLException, BookNotFoundException, ClassNotFoundException {
    String sql = "SELECT * FROM books WHERE book_id = ?";
    try (PreparedStatement stmt = ConnectionManager.getConnection().prepareStatement(sql)) {
      stmt.setInt(1, bookId);
      var resultSet = stmt.executeQuery();
      if (resultSet.next()) {
        Book book = new Book(
            resultSet.getInt("book_id"),
            resultSet.getString("title"),
            resultSet.getString("author"),
            resultSet.getInt("num_pages"));
        return Optional.of(book);
      } else {
        throw new BookNotFoundException("Book with ID " + bookId + " not found.");
      }
    }
  }

  @Override
  public boolean addBook(User user, Book book) throws SQLException, ClassNotFoundException, UserNotFoundException {
    Optional<User> existingUser = this.validateUser(user.getName(), user.getPassword());
    if (existingUser.isEmpty()) {
      throw new UserNotFoundException("User not found: " + user.getName());
    }
    if (existingUser.get().getClearance() < 1) {
      return false;
    }
    String sql = "INSERT INTO books (title, author, num_pages) VALUES (?, ?, ?)";
    try (PreparedStatement stmt = ConnectionManager.getConnection().prepareStatement(sql)) {
      stmt.setString(1, book.getTitle());
      stmt.setString(2, book.getAuthor());
      stmt.setInt(3, book.getNumPages());
      int rowsAffected = stmt.executeUpdate();
      return rowsAffected > 0;
    }
  }

  @Override
  public boolean updateBook(User user, Book book)
      throws SQLException, BookNotFoundException, ClassNotFoundException, UserNotFoundException {
    Optional<User> existingUser = this.validateUser(user.getName(), user.getPassword());
    if (existingUser.isEmpty()) {
      throw new UserNotFoundException("User not found: " + user.getName());
    }
    if (existingUser.get().getClearance() < 1) {
      return false;
    }
    String sql = "UPDATE books SET title = ?, author = ?, num_pages = ? WHERE book_id = ?";
    try (PreparedStatement stmt = ConnectionManager.getConnection().prepareStatement(sql)) {
      stmt.setString(1, book.getTitle());
      stmt.setString(2, book.getAuthor());
      stmt.setInt(3, book.getNumPages());
      stmt.setInt(4, book.getBookId());
      int rowsAffected = stmt.executeUpdate();
      if (rowsAffected > 0) {
        return true;
      } else {
        throw new BookNotFoundException("Book with ID " + book.getBookId() + " not found.");
      }
    }
  }

  @Override
  public boolean deleteBook(User user, Book book)
      throws SQLException, BookNotFoundException, ClassNotFoundException, UserNotFoundException {
    Optional<User> existingUser = this.validateUser(user.getName(), user.getPassword());
    if (existingUser.isEmpty()) {
      throw new UserNotFoundException("User not found: " + user.getName());
    }
    if (existingUser.get().getClearance() < 1) {
      return false;
    }
    String sql = "DELETE FROM books WHERE book_id = ?";
    try (PreparedStatement stmt = ConnectionManager.getConnection().prepareStatement(sql)) {
      stmt.setInt(1, book.getBookId());
      int rowsAffected = stmt.executeUpdate();
      if (rowsAffected > 0) {
        return true;
      } else {
        throw new BookNotFoundException("Book with ID " + book.getBookId() + " not found.");
      }
    }
  }

  @Override
  public List<Tracker> getUserProgress(User user) throws SQLException, UserNotFoundException, ClassNotFoundException {
    List<Tracker> trackers = new ArrayList<>();
    String sql = "SELECT b.book_id, b.title, b.author, b.num_pages, t.progress, t.rating " +
        "FROM books b JOIN trackers t ON b.book_id = t.book_id " +
        "WHERE t.user_id = ?";
    try (PreparedStatement stmt = ConnectionManager.getConnection().prepareStatement(sql)) {
      stmt.setInt(1, user.getUserId());
      var resultSet = stmt.executeQuery();
      while (resultSet.next()) {
        Book book = new Book(
            resultSet.getInt("book_id"),
            resultSet.getString("title"),
            resultSet.getString("author"),
            resultSet.getInt("num_pages"));
        int progress = resultSet.getInt("progress");
        int rating = resultSet.getInt("rating");
        trackers.add(new Tracker(book, progress, rating));
      }
      return trackers;
    }
  }

  @Override
  public Optional<Tracker> getUserProgress(User user, Book book)
      throws SQLException, UserNotFoundException, ClassNotFoundException {
    String sql = "select progress, rating from trackers where user_id = ? and book_id = ?";
    try (PreparedStatement stmt = ConnectionManager.getConnection().prepareStatement(sql)) {
      stmt.setInt(1, user.getUserId());
      stmt.setInt(2, book.getBookId());
      var resultSet = stmt.executeQuery();
      if (resultSet.next()) {
        int progress = resultSet.getInt("progress");
        int rating = resultSet.getInt("rating");
        return Optional.of(new Tracker(book, progress, rating));
      }
      return Optional.empty();
    }
  }

  @Override
  public List<UserProgress> getAllUsersProgress(Book book)
      throws SQLException, BookNotFoundException, ClassNotFoundException {
    String sql = "SELECT u.name, t.progress, t.rating " +
        "FROM users u JOIN trackers t ON u.user_id = t.user_id " +
        "WHERE t.book_id = ?";
    try (PreparedStatement stmt = ConnectionManager.getConnection().prepareStatement(sql)) {
      stmt.setInt(1, book.getBookId());
      var resultSet = stmt.executeQuery();
      List<UserProgress> userProgressList = new ArrayList<>();
      while (resultSet.next()) {
        String username = resultSet.getString("name");
        int progress = resultSet.getInt("progress");
        int rating = resultSet.getInt("rating");
        userProgressList.add(new UserProgress(username, progress, rating));
      }
      return userProgressList;
    }
  }

  @Override
  public boolean updateProgress(User user, Book book, int progress) throws SQLException, ClassNotFoundException {
    String sql = "INSERT INTO trackers (user_id, book_id, progress) VALUES (?, ?, ?) " +
        "ON DUPLICATE KEY UPDATE progress = ?";
    try (PreparedStatement stmt = ConnectionManager.getConnection().prepareStatement(sql)) {
      stmt.setInt(1, user.getUserId());
      stmt.setInt(2, book.getBookId());
      stmt.setInt(3, progress);
      stmt.setInt(4, progress);
      stmt.executeUpdate();
      int rowsAffected = stmt.executeUpdate();
      return rowsAffected > 0;
    }
  }

  @Override
  public boolean rateBook(User user, Book book, int rating)
      throws SQLException, ClassNotFoundException {
    String sql = "INSERT INTO trackers (user_id, book_id, rating) VALUES (?, ?, ?) " +
        "ON DUPLICATE KEY UPDATE rating = ?";
    try (PreparedStatement stmt = ConnectionManager.getConnection().prepareStatement(sql)) {
      stmt.setInt(1, user.getUserId());
      stmt.setInt(2, book.getBookId());
      stmt.setInt(3, rating);
      stmt.setInt(4, rating);
      int rowsAffected = stmt.executeUpdate();
      if (rowsAffected > 0) {
        return true;
      } else {
        throw new SQLException("Failed to rate book. User or book not found.");
      }
    }
  }

  @Override
  public Optional<Double> getAverageRating(Book book)
      throws SQLException, BookNotFoundException, ClassNotFoundException {
    String sql = "SELECT AVG(rating) AS average_rating FROM trackers WHERE book_id = ?";
    try (PreparedStatement stmt = ConnectionManager.getConnection().prepareStatement(sql)) {
      stmt.setInt(1, book.getBookId());
      var resultSet = stmt.executeQuery();
      if (resultSet.next()) {
        double averageRating = resultSet.getDouble("average_rating");
        return Optional.of(averageRating);
      }
      return Optional.empty();
    }
  }

  @Override
  public Optional<Integer> getUserRating(User user, Book book)
      throws SQLException, ClassNotFoundException {
    String sql = "SELECT rating FROM trackers WHERE user_id = ? AND book_id = ?";
    try (PreparedStatement stmt = ConnectionManager.getConnection().prepareStatement(sql)) {
      stmt.setInt(1, user.getUserId());
      stmt.setInt(2, book.getBookId());
      var resultSet = stmt.executeQuery();
      if (resultSet.next()) {
        int rating = resultSet.getInt("rating");
        return Optional.of(rating);
      }
      return Optional.empty();
    }
  }

}
