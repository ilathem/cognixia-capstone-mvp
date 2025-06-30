package com.cognixia.javafuturehorizons.capstone.dao;

import com.cognixia.javafuturehorizons.capstone.model.Book;
import com.cognixia.javafuturehorizons.capstone.model.Tracker;
import com.cognixia.javafuturehorizons.capstone.model.User;
import com.cognixia.javafuturehorizons.capstone.model.UserProgress;
import com.cognixia.javafuturehorizons.capstone.connection.ConnectionManager;
import com.cognixia.javafuturehorizons.capstone.exception.BookNotFoundException;
import com.cognixia.javafuturehorizons.capstone.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.List;
import java.util.Map;

public class DaoTest {
  private static Dao dao;

  @BeforeAll
  public static void beforeAll() throws SQLException, ClassNotFoundException {
    // This method can be used to set up any static resources needed for the tests
    // For example, initializing a database connection pool or loading test data
    dao = DaoImpl.getInstance();
  }

  @BeforeEach
  public void beforeEach() throws Exception {
    String sql_data_statements = new String(
        Files.readAllBytes(
            Paths.get(
                getClass().getClassLoader().getResource("test_data.sql").toURI())));
    try (Statement stmt = ConnectionManager.getConnection().createStatement()) {
      for (String sql : sql_data_statements.split(";")) {
        if (!sql.trim().isEmpty()) {
          stmt.executeUpdate(sql.trim());
        }
      }
    }
    // try (Statement stmt = ConnectionManager.getConnection().createStatement()) {
    // stmt.executeUpdate(
    // "INSERT INTO books (title, author, num_pages) VALUES ('Thus Spoke
    // Zarathustra', 'Friedrich Nietzsche', 327)");
    // stmt.executeUpdate("INSERT INTO books (title, author, num_pages) VALUES ('The
    // Republic', 'Plato', 416)");
    // stmt.executeUpdate(
    // "INSERT INTO books (title, author, num_pages) VALUES ('Nicomachean Ethics',
    // 'Aristotle', 400)");
    // stmt.executeUpdate(
    // "INSERT INTO books (title, author, num_pages) VALUES ('Meditations on First
    // Philosophy', 'René Descartes', 80)");
    // stmt.executeUpdate("INSERT INTO books (title, author, num_pages) VALUES
    // ('Apology', 'Socrates', 40)");
    // stmt.executeUpdate(
    // "INSERT INTO books (title, author, num_pages) VALUES ('Beyond Good and Evil',
    // 'Friedrich Nietzsche', 320)");
    // stmt.executeUpdate(
    // "INSERT INTO books (title, author, num_pages) VALUES ('Critique of Pure
    // Reason', 'Immanuel Kant', 856)");
    // stmt.executeUpdate(
    // "INSERT INTO books (title, author, num_pages) VALUES ('Being and Time',
    // 'Martin Heidegger', 589)");
    // stmt.executeUpdate("INSERT INTO books (title, author, num_pages) VALUES ('The
    // Symposium', 'Plato', 80)");
    // stmt.executeUpdate(
    // "INSERT INTO books (title, author, num_pages) VALUES ('The Prince', 'Niccolò
    // Machiavelli', 140)");
    // stmt.executeUpdate("INSERT INTO books (title, author, num_pages) VALUES
    // ('Leviathan', 'Thomas Hobbes', 736)");
    // stmt.executeUpdate(
    // "INSERT INTO books (title, author, num_pages) VALUES ('The Social Contract',
    // 'Jean-Jacques Rousseau', 192)");
    // stmt.executeUpdate(
    // "INSERT INTO books (title, author, num_pages) VALUES ('Tractatus
    // Logico-Philosophicus', 'Ludwig Wittgenstein', 232)");
    // stmt.executeUpdate(
    // "INSERT INTO books (title, author, num_pages) VALUES ('Phenomenology of
    // Spirit', 'G. W. F. Hegel', 640)");
    // stmt.executeUpdate("INSERT INTO books (title, author, num_pages) VALUES ('On
    // Liberty', 'John Stuart Mill', 176)");
    // } catch (SQLException e) {
    // throw new RuntimeException("Error setting up test environment", e);
    // }
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
  public void testCreateUser() throws SQLException, UserNotFoundException, ClassNotFoundException {
    User user = new User(0, "TestUser", "password");
    User result = dao.createUser(user);
    assertTrue(user.getName().equals(result.getName()) && user.getPassword().equals(result.getPassword()));
  }

  @Test
  public void testDeleteUser() throws SQLException, UserNotFoundException, ClassNotFoundException {
    User user = new User(0, "TestUser", "password");
    dao.createUser(user);
    boolean result = dao.deleteUser(user);
    assertTrue(result);
  }

  @Test
  public void testGetUserById() throws SQLException, UserNotFoundException, ClassNotFoundException {
    Optional<User> retrievedUser = dao.getUserById(1);
    assertTrue(retrievedUser.isPresent());
    assertEquals("testuser1", retrievedUser.get().getName());
  }

  @Test
  public void testValidateUser() throws SQLException, ClassNotFoundException {
    User user = new User(0, "TestUser", "password");
    dao.createUser(user);
    Optional<User> validatedUser = dao.validateUser("TestUser", "password");
    assertTrue(validatedUser.isPresent());
    assertEquals(user.getName(), validatedUser.get().getName());
  }

  @Test
  public void testGetBookById() throws SQLException, BookNotFoundException, ClassNotFoundException {
    Optional<Book> book = dao.getBookById(1);
    assertTrue(book.isPresent());
    assertEquals("Thus Spoke Zarathustra", book.get().getTitle());
  }

  @Test
  public void testAddBook() throws SQLException, ClassNotFoundException, UserNotFoundException {
    Book book = new Book("Test Book", "Test Author", 100);
    User user = new User("admin", "admin");
    boolean result = dao.addBook(user, book);
    assertTrue(result);
  }

  @Test
  public void testUpdateBook()
      throws SQLException, BookNotFoundException, ClassNotFoundException, UserNotFoundException {
    String updatedTitle = "Thus Spake Zarathustra";
    Book book = new Book(1, updatedTitle, "Friedrich Nietzsche", 200);
    User user = new User("admin", "admin");
    boolean result = dao.updateBook(user, book);
    assertTrue(result);

    Optional<Book> updatedBook = dao.getBookById(1);
    assertTrue(updatedBook.isPresent());
    assertEquals(updatedTitle, updatedBook.get().getTitle());
  }

  @Test
  public void testDeleteBook()
      throws SQLException, BookNotFoundException, ClassNotFoundException, UserNotFoundException {
    Book book = dao.getBookById(1).get();
    User user = new User("admin", "admin");
    boolean result = dao.deleteBook(user, book);
    assertTrue(result);
    assertThrows(BookNotFoundException.class, () -> dao.getBookById(1));
  }

  @Test
  public void testGetUserProgressForAllBooks()
      throws SQLException, UserNotFoundException, BookNotFoundException, ClassNotFoundException {
    User user = new User(1, 0, "TestUser", "password");
    List<Tracker> progress = dao.getUserProgress(user);
    assertEquals(10, progress.get(0).getProgress());
    assertEquals(20, progress.get(1).getProgress());
  }

  @Test
  public void testGetUserProgressForSpecificBook()
      throws ClassNotFoundException, SQLException, UserNotFoundException, BookNotFoundException {
    User user = new User(1, 0, "TestUser", "password");
    Book book = new Book(1, "Thus Spoke Zarathustra", "Friedrich Nietzsche", 327);
    Integer progress = dao.getUserProgress(user, book).get().getProgress();
    assertEquals(10, progress.intValue());
  }

  @Test
  public void testGetAllUsersProgressForBook()
      throws SQLException, BookNotFoundException, UserNotFoundException, ClassNotFoundException {
    Book book = new Book(1, "Thus Spoke Zarathustra", "Friedrich Nietzsche", 327);
    List<UserProgress> progress = dao.getAllUsersProgress(book);
    assertEquals(10, progress.get(0).getProgress());
    assertEquals(50, progress.get(1).getProgress());
    assertEquals(120, progress.get(2).getProgress());
    assertEquals(200, progress.get(3).getProgress());
    assertEquals(327, progress.get(4).getProgress());
  }

  @Test
  public void testUpdateProgress()
      throws SQLException, UserNotFoundException, BookNotFoundException, ClassNotFoundException {
    User user = new User(1, 0, "TestUser", "password");
    Book book = new Book(1, "Thus Spoke Zarathustra", "Friedrich Nietzsche", 327);
    dao.updateProgress(user, book, 5);
    Optional<Tracker> progressTracker = dao.getUserProgress(user, book);
    assertTrue(progressTracker.isPresent());
    assertEquals(5, progressTracker.get().getProgress());
  }

  @Test
  public void testRateBook() throws SQLException, UserNotFoundException, BookNotFoundException, ClassNotFoundException {
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
  public void testGetAverageRating()
      throws SQLException, BookNotFoundException, UserNotFoundException, ClassNotFoundException {
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