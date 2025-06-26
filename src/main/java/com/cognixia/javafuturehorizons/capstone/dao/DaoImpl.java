package com.cognixia.javafuturehorizons.capstone.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.cognixia.javafuturehorizons.capstone.connection.ConnectionManager;
import com.cognixia.javafuturehorizons.capstone.exception.BookNotFoundException;
import com.cognixia.javafuturehorizons.capstone.exception.UserNotFoundException;
import com.cognixia.javafuturehorizons.capstone.model.Book;
import com.cognixia.javafuturehorizons.capstone.model.User;

public class DaoImpl implements Dao {

  private Connection connection = null;
  private static DaoImpl instance;

  private DaoImpl() throws SQLException, ClassNotFoundException {
    establishConnection();
  }

  public static DaoImpl getInstance() throws ClassNotFoundException, SQLException {
    if (instance == null) {
      instance = new DaoImpl();
    }
    return instance;
  }

  @Override
  public void establishConnection() throws SQLException, ClassNotFoundException {
    if (connection == null) {
      connection = ConnectionManager.getConnection();
    }
  }

  @Override
  public void closeConnection() throws SQLException {
    if (connection != null)
      connection.close();
  }

  @Override
  public User createUser(User user) throws SQLException {
    String sql = "INSERT INTO users (name, password) VALUES (?, ?)";
    PreparedStatement stmt = connection.prepareStatement(sql);
    stmt.setString(1, user.getName());
    stmt.setString(2, user.getPassword());
    stmt.executeUpdate();
    return this.validateUser(user.getName(), user.getPassword()).get();
  }

  @Override
  public boolean deleteUser(User user) throws SQLException, UserNotFoundException {
    String sql = "DELETE FROM users WHERE name = ?";
    PreparedStatement stmt = connection.prepareStatement(sql);
    stmt.setString(1, user.getName());
    int rowsAffected = stmt.executeUpdate();
    return rowsAffected > 0;
  }

  @Override
  public Optional<User> getUserById(int userId) throws SQLException, UserNotFoundException {
    String sql = "SELECT * FROM users WHERE user_id = ?";
    PreparedStatement stmt = connection.prepareStatement(sql);
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

  @Override
  public Optional<User> validateUser(String username, String password) throws SQLException {
    String sql = "SELECT * FROM users WHERE name = ? AND password = ?";
    PreparedStatement stmt = connection.prepareStatement(sql);
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

  @Override
  public Optional<Book> getBookById(int bookId) throws SQLException, BookNotFoundException {
    String sql = "SELECT * FROM books WHERE book_id = ?";
    PreparedStatement stmt = connection.prepareStatement(sql);
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

  @Override
  public boolean addBook(Book book) throws SQLException {
    String sql = "INSERT INTO books (title, author, num_pages) VALUES (?, ?, ?)";
    PreparedStatement stmt = connection.prepareStatement(sql);
    stmt.setString(1, book.getTitle());
    stmt.setString(2, book.getAuthor());
    stmt.setInt(3, book.getNumPages());
    int rowsAffected = stmt.executeUpdate();
    return rowsAffected > 0;
  }

  @Override
  public boolean updateBook(Book book) throws SQLException, BookNotFoundException {
    String sql = "UPDATE books SET title = ?, author = ?, num_pages = ? WHERE book_id = ?";
    PreparedStatement stmt = connection.prepareStatement(sql);
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

  @Override
  public boolean deleteBook(Book book) throws SQLException, BookNotFoundException {
    String sql = "DELETE FROM books WHERE book_id = ?";
    PreparedStatement stmt = connection.prepareStatement(sql);
    stmt.setInt(1, book.getBookId());
    int rowsAffected = stmt.executeUpdate();
    if (rowsAffected > 0) {
      return true;
    } else {
      throw new BookNotFoundException("Book with ID " + book.getBookId() + " not found.");
    }
  }

  @Override
  public Map<Book, Integer> getUserProgress(User user) throws SQLException, UserNotFoundException {
    Map<Book, Integer> progressMap = new HashMap<>();
    String sql = "SELECT b.book_id, b.title, b.author, b.num_pages, t.progress " +
        "FROM books b JOIN trackers t ON b.book_id = t.book_id " +
        "WHERE t.user_id = ?";
    PreparedStatement stmt = connection.prepareStatement(sql);
    stmt.setInt(1, user.getUserId());
    var resultSet = stmt.executeQuery();
    while (resultSet.next()) {
      Book book = new Book(
          resultSet.getInt("book_id"),
          resultSet.getString("title"),
          resultSet.getString("author"),
          resultSet.getInt("num_pages"));
      int progress = resultSet.getInt("progress");
      progressMap.put(book, progress);
      System.out.println("Book: " + book.getTitle() + ", Progress: " + progress);
    }
    return progressMap;
  }

  @Override
  public Optional<Integer> getUserProgress(User user, Book book) throws SQLException, UserNotFoundException {
    String sql = "select progress from trackers where user_id = ? and book_id = ?";
    PreparedStatement stmt = connection.prepareStatement(sql);
    stmt.setInt(1, user.getUserId());
    stmt.setInt(2, book.getBookId());
    var resultSet = stmt.executeQuery();
    if (resultSet.next()) {
      int progress = resultSet.getInt("progress");
      return Optional.of(progress);
    }
    return Optional.empty();
  }

  @Override
  public Map<User, Integer> getAllUsersProgress(Book book) throws SQLException, BookNotFoundException {
    String sql = "SELECT u.user_id, u.name, t.progress " +
        "FROM users u JOIN trackers t ON u.user_id = t.user_id " +
        "WHERE t.book_id = ?";
    PreparedStatement stmt = connection.prepareStatement(sql);
    stmt.setInt(1, book.getBookId());
    var resultSet = stmt.executeQuery();
    Map<User, Integer> userProgressMap = new HashMap<>();
    while (resultSet.next()) {
      User user = new User(
          resultSet.getInt("user_id"),
          resultSet.getString("name"));
      int progress = resultSet.getInt("progress");
      userProgressMap.put(user, progress);
    }
    return userProgressMap;
  }

  @Override
  public boolean updateProgress(User user, Book book, int progress) throws SQLException {
    String sql = "UPDATE trackers SET progress = ? WHERE user_id = ? AND book_id = ?";
    PreparedStatement stmt = connection.prepareStatement(sql);
    stmt.setInt(1, progress);
    stmt.setInt(2, user.getUserId());
    stmt.setInt(3, book.getBookId());
    int rowsAffected = stmt.executeUpdate();
    return rowsAffected > 0;
  }

  @Override
  public boolean rateBook(User user, Book book, int rating)
      throws SQLException {
      String sql = "INSERT INTO trackers (user_id, book_id, rating) VALUES (?, ?, ?) " +
          "ON DUPLICATE KEY UPDATE rating = ?";
      PreparedStatement stmt = connection.prepareStatement(sql);
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

  @Override
  public Optional<Double> getAverageRating(Book book) throws SQLException, BookNotFoundException {
    String sql = "SELECT AVG(rating) AS average_rating FROM trackers WHERE book_id = ?";
    PreparedStatement stmt = connection.prepareStatement(sql);
    stmt.setInt(1, book.getBookId());
    var resultSet = stmt.executeQuery();
    if (resultSet.next()) {
      double averageRating = resultSet.getDouble("average_rating");
      return Optional.of(averageRating);
    }
    return Optional.empty();
  }

  @Override
  public Optional<Integer> getUserRating(User user, Book book)
      throws SQLException {
      String sql = "SELECT rating FROM trackers WHERE user_id = ? AND book_id = ?";
      PreparedStatement stmt = connection.prepareStatement(sql);
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
