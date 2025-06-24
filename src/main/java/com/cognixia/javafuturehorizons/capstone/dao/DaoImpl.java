package com.cognixia.javafuturehorizons.capstone.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

import com.cognixia.javafuturehorizons.capstone.connection.ConnectionManager;
import com.cognixia.javafuturehorizons.capstone.exception.BookNotFoundException;
import com.cognixia.javafuturehorizons.capstone.exception.UserNotFoundException;
import com.cognixia.javafuturehorizons.capstone.model.Book;
import com.cognixia.javafuturehorizons.capstone.model.User;

public class DaoImpl implements Dao {

  private Connection connection = null;

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
  public boolean createUser(User user) throws SQLException {
    String sql = "INSERT INTO users (name, password, clearance) VALUES (?, ?, ?)";
    PreparedStatement stmt = connection.prepareStatement(sql);
    stmt.setString(1, user.getName());
    stmt.setString(2, user.getPassword());
    stmt.setInt(3, user.getClearance());
    int rowsAffected = stmt.executeUpdate();
    return rowsAffected > 0;
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
          resultSet.getString("password")
      );
      return Optional.of(user);
    } else {
      throw new UserNotFoundException("User with ID " + userId + " not found.");
    } 
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
          resultSet.getInt("num_pages")
      );
      return Optional.of(book);
    } else {  
      throw new BookNotFoundException("Book with ID " + bookId + " not found.");
    } 
  }

  @Override
  public boolean addBook(Book book) throws SQLException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'addBook'");
  }

  @Override
  public boolean updateBook(Book book) throws SQLException, BookNotFoundException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'updateBook'");
  }

  @Override
  public boolean deleteBook(Book book) throws SQLException, BookNotFoundException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteBook'");
  }

  @Override
  public Map<Book, Integer> getUserProgress(User user) throws SQLException, UserNotFoundException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getUserProgress'");
  }

  @Override
  public Optional<Integer> getUserProgress(User user, Book book) throws SQLException, UserNotFoundException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getUserProgress'");
  }

  @Override
  public Map<User, Integer> getAllUsersProgress(Book book) throws SQLException, BookNotFoundException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getAllUsersProgress'");
  }

  @Override
  public boolean updateProgress(User user, Book book, int progress)
      throws SQLException, UserNotFoundException, BookNotFoundException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'updateProgress'");
  }

  @Override
  public boolean rateBook(User user, Book book, int rating)
      throws SQLException, UserNotFoundException, BookNotFoundException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'rateBook'");
  }

  @Override
  public Optional<Double> getAverageRating(Book book) throws SQLException, BookNotFoundException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getAverageRating'");
  }

  @Override
  public Optional<Integer> getUserRating(User user, Book book)
      throws SQLException, UserNotFoundException, BookNotFoundException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getUserRating'");
  }

}
