package com.cognixia.javafuturehorizons.capstone.dao;

import com.cognixia.javafuturehorizons.capstone.model.Book;
import com.cognixia.javafuturehorizons.capstone.model.User;
import com.cognixia.javafuturehorizons.capstone.connection.ConnectionManager;
import com.cognixia.javafuturehorizons.capstone.exception.BookNotFoundException;
import com.cognixia.javafuturehorizons.capstone.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.Map;

public class DaoTest {
  private static Dao dao;

  @BeforeAll
  public static void beforeAll() throws SQLException, ClassNotFoundException {
    // This method can be used to set up any static resources needed for the tests
    // For example, initializing a database connection pool or loading test data
    dao = new DaoImpl();
    dao.establishConnection();
  }

  @AfterAll
  public static void afterAll() throws SQLException {
    // This method can be used to clean up resources after all tests have run
    if (dao != null) {
      dao.closeConnection();
    }
  }

  @BeforeEach
  public void beforeEach() throws Exception {
    try (Statement stmt = ConnectionManager.getConnection().createStatement()) {
      stmt.executeUpdate(
          "INSERT INTO books (title, author, num_pages) VALUES ('Thus Spoke Zarathustra', 'Friedrich Nietzsche', 327)");
      stmt.executeUpdate("INSERT INTO books (title, author, num_pages) VALUES ('The Republic', 'Plato', 416)");
      stmt.executeUpdate(
          "INSERT INTO books (title, author, num_pages) VALUES ('Nicomachean Ethics', 'Aristotle', 400)");
      stmt.executeUpdate(
          "INSERT INTO books (title, author, num_pages) VALUES ('Meditations on First Philosophy', 'René Descartes', 80)");
      stmt.executeUpdate("INSERT INTO books (title, author, num_pages) VALUES ('Apology', 'Socrates', 40)");
      stmt.executeUpdate(
          "INSERT INTO books (title, author, num_pages) VALUES ('Beyond Good and Evil', 'Friedrich Nietzsche', 320)");
      stmt.executeUpdate(
          "INSERT INTO books (title, author, num_pages) VALUES ('Critique of Pure Reason', 'Immanuel Kant', 856)");
      stmt.executeUpdate(
          "INSERT INTO books (title, author, num_pages) VALUES ('Being and Time', 'Martin Heidegger', 589)");
      stmt.executeUpdate("INSERT INTO books (title, author, num_pages) VALUES ('The Symposium', 'Plato', 80)");
      stmt.executeUpdate(
          "INSERT INTO books (title, author, num_pages) VALUES ('The Prince', 'Niccolò Machiavelli', 140)");
      stmt.executeUpdate("INSERT INTO books (title, author, num_pages) VALUES ('Leviathan', 'Thomas Hobbes', 736)");
      stmt.executeUpdate(
          "INSERT INTO books (title, author, num_pages) VALUES ('The Social Contract', 'Jean-Jacques Rousseau', 192)");
      stmt.executeUpdate(
          "INSERT INTO books (title, author, num_pages) VALUES ('Tractatus Logico-Philosophicus', 'Ludwig Wittgenstein', 232)");
      stmt.executeUpdate(
          "INSERT INTO books (title, author, num_pages) VALUES ('Phenomenology of Spirit', 'G. W. F. Hegel', 640)");
      stmt.executeUpdate("INSERT INTO books (title, author, num_pages) VALUES ('On Liberty', 'John Stuart Mill', 176)");
    } catch (SQLException e) {
      throw new RuntimeException("Error setting up test environment", e);
    }
  }

  @AfterEach
  public void afterEach() throws Exception {
    try (Statement stmt = ConnectionManager.getConnection().createStatement()) {
      stmt.executeUpdate("set foreign_key_checks = 0");
      stmt.executeUpdate("truncate table books");
      stmt.executeUpdate("truncate table users");
      stmt.executeUpdate("truncate table trackers");
      stmt.executeUpdate("set foreign_key_checks = 1");
    } catch (SQLException e) {
      throw new RuntimeException("Error tearing down test environment", e);
    }
  }

  @Test
  public void testCreateUser() throws SQLException, UserNotFoundException {
    User user = new User(0, "TestUser", "password");
    boolean result = dao.createUser(user);
    assertTrue(result);
  }

  @Test
  public void testDeleteUser() throws SQLException, UserNotFoundException {
    User user = new User(0, "TestUser", "password");
    dao.createUser(user);
    boolean result = dao.deleteUser(user);
    assertTrue(result);
  }

  @Test
  public void testGetUserById() throws SQLException, UserNotFoundException {
    User user = new User(0, "TestUser", "password");
    dao.createUser(user);
    Optional<User> retrievedUser = dao.getUserById(1);
    assertTrue(retrievedUser.isPresent());
    assertEquals(user.getName(), retrievedUser.get().getName());
  }

  @Test
  public void testGetBookById() throws SQLException, BookNotFoundException {
    Optional<Book> book = dao.getBookById(1);
    assertTrue(book.isPresent());
    assertEquals("Thus Spoke Zarathustra", book.get().getTitle());
  }

  @Test
  public void testAddBook() throws SQLException {
    Book book = new Book("Test Book", "Test Author", 100);
    boolean result = dao.addBook(book);
    assertTrue(result);
  }

  @Test
  public void testUpdateBook() throws SQLException, BookNotFoundException {
    String updatedTitle = "Thus Spake Zarathustra";
    Book book = new Book(1, updatedTitle, "Friedrich Nietzsche", 200);
    boolean result = dao.updateBook(book);
    assertTrue(result);

    Optional<Book> updatedBook = dao.getBookById(1);
    assertTrue(updatedBook.isPresent());
    assertEquals(updatedTitle, updatedBook.get().getTitle());
  }

  @Test
  public void testDeleteBook() throws SQLException, BookNotFoundException {
    Book book = dao.getBookById(1).get();
    boolean result = dao.deleteBook(book);
    assertTrue(result);
    assertThrows(BookNotFoundException.class, () -> dao.getBookById(1));
  }

  @Test
  public void testGetUserProgressForAllBooks()
      throws SQLException, UserNotFoundException, BookNotFoundException, ClassNotFoundException {
    User user = new User(1, 0, "TestUser", "password");
    Book book1 = new Book(1, "Thus Spoke Zarathustra", "Friedrich Nietzsche", 327);
    Book book2 = new Book(2, "The Republic", "Plato", 416);
    try (Statement stmt = ConnectionManager.getConnection().createStatement()) {
      stmt.executeUpdate("insert into users (name, password) values ('TestUser', 'password')");
      stmt.executeUpdate("insert into trackers (user_id, book_id, progress) values (1, 1, 3)");
      stmt.executeUpdate("insert into trackers (user_id, book_id, progress) values (1, 2, 3)");
    } catch (SQLException e) {
      throw new RuntimeException("Error setting up test environment", e);
    }
    Map<Book, Integer> progress = dao.getUserProgress(user);
    Map<Book, Integer> expectedProgress = Map.of(
        book1, 3,
        book2, 3);
    assertEquals(expectedProgress, progress);
  }

  @Test
  public void testGetUserProgressForSpecificBook()
      throws ClassNotFoundException, SQLException, UserNotFoundException, BookNotFoundException {
    User user = new User(1, 0, "TestUser", "password");
    Book book = new Book(1, "Thus Spoke Zarathustra", "Friedrich Nietzsche", 327);
    try (Statement stmt = ConnectionManager.getConnection().createStatement()) {
      stmt.executeUpdate("insert into users (name, password) values ('TestUser', 'password')");
      stmt.executeUpdate("insert into trackers (user_id, book_id, progress) values (1, 1, 5)");
    } catch (SQLException e) {
      throw new RuntimeException("Error setting up test environment", e);
    }
    Integer progress = dao.getUserProgress(user, book).get();
    assertEquals(5, progress.intValue());
  }

  @Test
  public void testGetAllUsersProgressForBook() throws SQLException, BookNotFoundException, UserNotFoundException, ClassNotFoundException {
    Book book = new Book(1, "Thus Spoke Zarathustra", "Friedrich Nietzsche", 327);
    User user1 = new User(1, 0, "User1", "password1");
    User user2 = new User(2, 0, "User2", "password2");
    try (Statement stmt = ConnectionManager.getConnection().createStatement()) {
      stmt.executeUpdate("insert into users (name, password) values ('User1', 'password1')");
      stmt.executeUpdate("insert into users (name, password) values ('User2', 'password2')");
      stmt.executeUpdate("insert into trackers (user_id, book_id, progress) values (1, 1, 5)");
      stmt.executeUpdate("insert into trackers (user_id, book_id, progress) values (2, 1, 3)");
    } catch (SQLException e) {
      throw new RuntimeException("Error setting up test environment", e);
    }
    Map<User, Integer> progress = dao.getAllUsersProgress(book);
    Map<User, Integer> expectedProgress = Map.of(
        user1, 5,
        user2, 3);
    assertEquals(expectedProgress, progress);
  }

  @Test
  public void testUpdateProgress() throws SQLException, UserNotFoundException, BookNotFoundException, ClassNotFoundException {
    User user = new User(1, 0, "TestUser", "password");
    Book book = new Book(1, "Thus Spoke Zarathustra", "Friedrich Nietzsche", 327);
    try (Statement stmt = ConnectionManager.getConnection().createStatement()) {
      stmt.executeUpdate("insert into users (name, password) values ('TestUser', 'password')");
      stmt.executeUpdate("insert into trackers (user_id, book_id, progress) values (1, 1, 0)");
    } catch (SQLException e) {
      throw new RuntimeException("Error setting up test environment", e);
    }
    dao.updateProgress(user, book, 5);
    Optional<Integer> progress = dao.getUserProgress(user, book);
    assertTrue(progress.isPresent());
    assertEquals(5, progress.get().intValue());
  }

  @Test
  public void testRateBook() throws SQLException, UserNotFoundException, BookNotFoundException {
    User user = new User(1, 0, "TestUser", "password");
    Book book = new Book(1, "Thus Spoke Zarathustra", "Friedrich Nietzsche", 327);
    dao.createUser(user);
    boolean result = dao.rateBook(user, book, 5);
    assertTrue(result);
    Optional<Integer> rating = dao.getUserRating(user, book);
    assertTrue(rating.isPresent());
    assertEquals(5, rating.get().intValue());
  }

  @Test
  public void testGetAverageRating() throws SQLException, BookNotFoundException, UserNotFoundException {
    Book book = dao.getBookById(1).get();
    User user1 = new User(1, 0, "User1", "password");
    User user2 = new User(2, 0, "User2", "password");
    dao.createUser(user1);
    dao.createUser(user2);
    dao.rateBook(user1, book, 5);
    dao.rateBook(user2, book, 3);
    Optional<Double> averageRating = dao.getAverageRating(book);
    assertTrue(averageRating.isPresent());
    assertEquals(4.0, averageRating.get().doubleValue());
  }

}