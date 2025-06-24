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
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteUser'");
  }

  @Override
  public Optional<User> getUserById(int userId) throws SQLException, UserNotFoundException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getUserById'");
  }

  @Override
  public Optional<Book> getBookById(int bookId) throws SQLException, BookNotFoundException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getBookById'");
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
