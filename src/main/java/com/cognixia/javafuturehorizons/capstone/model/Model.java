package com.cognixia.javafuturehorizons.capstone.model;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.cognixia.javafuturehorizons.capstone.dao.Dao;
import com.cognixia.javafuturehorizons.capstone.dao.DaoImpl;
import com.cognixia.javafuturehorizons.capstone.exception.BookNotFoundException;
import com.cognixia.javafuturehorizons.capstone.exception.UserNotFoundException;
import com.cognixia.javafuturehorizons.capstone.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * Model class for the Tracker application.
 * 
 * Business Logic
 * 
 * This is concerned with the "what" of data access, not the "how" or "why".
 * 
 * Contains the business logic of the application.
 * 
 * Receives from the Controller raw JSON, then validates the JSON, converts it
 * to Java objects, processes it with the Dao, and returns the result to the 
 * Controller in JSON.
 */
public class Model {

  private static Model instance;
  private ObjectMapper objectMapper;

  private Model() {
    // Private constructor to prevent instantiation
    this.objectMapper = new ObjectMapper();
  }

  public static Model getInstance() {
    if (instance == null) {
      instance = new Model();
    }
    return instance;
  }

  public String processRequest(String jsonRequest) {
    // System.out.println("Processing request: " + jsonRequest);
    try {
      Request request = objectMapper.readValue(jsonRequest, Request.class);

      // Process the request
      Response response = processBusinessLogic(request);

      // Convert the response to JSON
      return objectMapper.writeValueAsString(response);

    } catch (BookNotFoundException e) {
      e.printStackTrace();
      return "{\"error\": \"Book not found.\"}";
    } catch (UserNotFoundException e) {
      e.printStackTrace();
      return "{\"error\": \"User not found.\"}";
    } catch (SQLException e) {
      e.printStackTrace();
      return "{\"error\": \"Database error occurred.\"}";
    } catch (ClassCastException e) {
      e.printStackTrace();
      return "{\"error\": \"Invalid data type in request.\"}";
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      return "{\"error\": \"Database driver not found.\"}";
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
      return "{\"error\": \"Invalid request format.\"}";
    } catch (UnsupportedOperationException e) {
      e.printStackTrace();
      return "{\"error\": \"Business logic not implemented.\"}";
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return "{\"error\": \"Error processing JSON request.\"}";
    } catch (Exception e) {
      e.printStackTrace();
      return "{\"error\": \"An error occurred while processing the request.\"}";
    }
  }

  private Response processBusinessLogic(Request request)
      throws SQLException, ClassNotFoundException, UserNotFoundException, BookNotFoundException, ClassCastException {
    Dao dao = DaoImpl.getInstance();

    // Process the request using the DAO
    switch (request.getAction()) {
      case "createUser": {
        User newUser = Utils.getUserFromRequest(request);
        User userCreated = dao.createUser(newUser);
        return new Response("User created successfully.", Map.of("user", userCreated));
      }
      case "deleteUser": {
        User userToDelete = Utils.getUserFromRequest(request);
        boolean userDeleted = dao.deleteUser(userToDelete);
        return userDeleted ? new Response("User deleted successfully.")
            : new Response("Failed to delete user.");
      }
      case "getUser": {
        int userId = (int) request.getData().get("userId");
        Optional<User> user = dao.getUserById(userId);
        return user.map(u -> new Response("User retrieved successfully.", Map.of("user", u)))
            .orElseGet(() -> new Response("User not found."));
      }
      case "login": {
        String username = (String) request.getData().get("username");
        String password = (String) request.getData().get("password");
        Optional<User> user = dao.validateUser(username, password);
        return user.map(u -> new Response("Login successful.", Map.of("user", u)))
            .orElseGet(() -> new Response("Invalid username or password."));
      }
      case "getAllBooks": {
        List<Book> books = dao.getAllBooks();
        return new Response("All books retrieved successfully.", Map.of("books", books));
      }
      case "getBook": {
        int bookId = (int) request.getData().get("bookId");
        Optional<Book> book = dao.getBookById(bookId);
        return book.map(b -> new Response("Book retrieved successfully.", Map.of("book", b)))
            .orElseGet(() -> new Response("Book not found."));
      }
      case "addBook": {
        Book newBook = (Book) request.getData().get("book");
        boolean bookAdded = dao.addBook(newBook);
        return bookAdded ? new Response("Book added successfully.")
            : new Response("Failed to add book.");
      }
      case "updateBook": {
        Book bookToUpdate = (Book) request.getData().get("book");
        boolean bookUpdated = dao.updateBook(bookToUpdate);
        return bookUpdated ? new Response("Book updated successfully.")
            : new Response("Failed to update book.");
      }
      case "deleteBook": {
        Book bookToDelete = (Book) request.getData().get("book");
        boolean bookDeleted = dao.deleteBook(bookToDelete);
        return bookDeleted ? new Response("Book deleted successfully.")
            : new Response("Failed to delete book.");
      }
      case "getUserProgress": {
        User userForProgress = Utils.getUserFromRequest(request);
        List<Tracker> progressList = dao.getUserProgress(userForProgress);
        return new Response("User progress retrieved successfully.", Map.of("progressMap", progressList));
      }
      case "getAllUsersProgress": {
        Book bookForProgress = (Book) objectMapper.convertValue(
            request.getData().get("book"), Book.class);
        List<UserProgress> allUsersProgress = dao.getAllUsersProgress(bookForProgress);
        return new Response("All users progress retrieved successfully.", Map.of("progressList", allUsersProgress));
      }
      case "updateProgress": {
        User userForProgress = objectMapper.convertValue(
            request.getData().get("user"), User.class);
        Book bookForUpdate = objectMapper.convertValue(
            request.getData().get("book"), Book.class);
        int newProgress = (int) request.getData().get("progress");
        boolean progressUpdated = false;
        try {
          progressUpdated = dao.updateProgress(userForProgress, bookForUpdate, newProgress);
        } catch (SQLException e) {
          e.printStackTrace();
          return new Response("Database error occurred while updating progress.");
        }
        return progressUpdated ? new Response("Progress updated successfully.")
            : new Response("Failed to update progress.");
      }
      case "rateBook": {
        User userForRating = (User) objectMapper.convertValue(request.getData().get("user"), User.class);
        Book bookForRating = (Book) objectMapper.convertValue(request.getData().get("book"), Book.class);
        int rating = (int) request.getData().get("rating");
        List<Tracker> progressList = dao.getUserProgress(userForRating);
        if (progressList.stream().noneMatch(tracker -> (tracker.getBook().equals(bookForRating) && tracker.getProgress() == bookForRating.getNumPages()))) {
          return new Response("User has not read the book, cannot rate.");
        }
        boolean bookRated = dao.rateBook(userForRating, bookForRating, rating);
        return bookRated ? new Response("Book rated successfully.")
            : new Response("Failed to rate book.");
      }
      default: {
        throw new UnsupportedOperationException("Unknown action: " + request.getAction());
      }
    }
  }

}
