package com.cognixia.javafuturehorizons.capstone.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.cognixia.javafuturehorizons.capstone.model.Book;
import com.cognixia.javafuturehorizons.capstone.model.Model;
import com.cognixia.javafuturehorizons.capstone.model.Request;
import com.cognixia.javafuturehorizons.capstone.model.Response;
import com.cognixia.javafuturehorizons.capstone.model.Tracker;
import com.cognixia.javafuturehorizons.capstone.model.User;
import com.cognixia.javafuturehorizons.capstone.model.UserProgress;
import com.cognixia.javafuturehorizons.capstone.utils.Utils;
import com.cognixia.javafuturehorizons.capstone.view.View;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * Controller class for the Tracker application.
 * 
 * Application / Control Flow Logic
 * 
 * Maps user actions to Model operations.
 * 
 * User gives input to View, View updates Controller, based on user input,
 * Controller updates Model, Model updates Controller, which updates View.
 */
public class Controller {

  private View view;
  private Model model;
  private static Controller instance;
  private ObjectMapper objectMapper;
  private User currentUser;

  private Controller() {
    this.view = View.getInstance();
    this.model = Model.getInstance();
    this.objectMapper = new ObjectMapper();
  }

  public static Controller getInstance() {
    if (instance == null) {
      instance = new Controller();
    }
    return instance;
  }

  private Response sendRequest(Request request) {
    try {
      String jsonResponse = model.processRequest(objectMapper.writeValueAsString(request));
      return objectMapper.readValue(jsonResponse, Response.class);
    } catch (Exception e) {
      view.printMessage("Error processing request: " + e.getMessage());
      return null;
    }
  }

  public void start() {
    welcome();
    showMainMenu();
  }

  private void welcome() {
    view.printMessage("Welcome to the Tracker Application!");
    int userChoice = view.getUserMenuChoice(new String[] {
        "Login",
        "Register"
    });
    switch (userChoice) {
      case 1:
        login();
        break;
      case 2:
        register();
        break;
      default:
        view.printMessage("Invalid choice. Please try again.");
        welcome();
        break;
    }
  }

  private void login() {
    String name = view.getUserInput("Enter your username: ");
    String password = view.getUserInput("Enter your password: ");
    Response res = sendRequest(new Request("login", Map.of("username", name, "password", password)));
    String message = res.getMessage();
    view.printMessage(message);
    if (message.contains("success")) {
      try {
        this.currentUser = Utils.getUserFromResponse(res);
        System.out.println(this.currentUser);
        showMainMenu();
      } catch (Exception e) {
        view.printMessage("Error retrieving user data: " + e.getMessage());
        this.currentUser = null;
        login();
      }
    } else {
      login();
    }
  }

  private void register() {
    String name = view.getUserInput("Enter new username: ");
    String password = view.getUserInput("Enter new password: ");
    User newUser = new User(name, password);
    Response res = sendRequest(new Request("createUser", Map.of("user", newUser)));
    this.currentUser = Utils.getUserFromResponse(res);
    view.printMessage(res.getMessage());
    if (this.currentUser != null) {
      System.out.println(this.currentUser);
      showMainMenu();
    } else {
      register();
    }
  }

  private void showMainMenu() {
    int menuChoice = view.getUserMenuChoice(new String[] {
        "View My Trackers",
        "Update Tracker Progress",
        "View All Users Progress For One Book",
        "Rate book that has been read"
    });
    switch (menuChoice) {
      case 1:
        showTrackers();
        break;
      case 2:
        updateTrackerProgress();
        break;
      case 3:
        showTrackersForOneBook();
        break;
      case 4:
        rateBook();
        break;
      default:
        break;
    }
  }

  private void rateBook() {
    List<Tracker> completedTrackers = getUserTrackers().stream()
        .filter(tracker -> tracker.getProgress() == tracker.getBook().getNumPages())
        .collect(Collectors.toList());
    view.printMessage("\n\nSelect a book to rate:");
    completedTrackers.forEach(tracker -> {
      view.printMessage("Book: " + tracker.getBook().getTitle() + ", Author: " + tracker.getBook().getAuthor());
    });
    String userInput = view.getUserInput("\nEnter the book title to rate: ");
    Book selectedBook = completedTrackers.stream()
        .filter(tracker -> tracker.getBook().getTitle().equalsIgnoreCase(userInput))
        .findFirst()
        .map(Tracker::getBook)
        .orElse(null);
    if (selectedBook != null) {
      int rating = Integer.parseInt(view.getUserInt("\nEnter your rating for this book (1-5): ", 1, 5));
      Response response = sendRequest(new Request("rateBook", Map.of(
          "user", currentUser,
          "book", selectedBook,
          "rating", rating)));
      view.printMessage(response.getMessage());
    } else {
      view.printMessage("No book found with title: " + userInput);
    }
    showMainMenu();
  }

  private void showTrackersForOneBook() {
    List<Book> books = getAllBooks();
    view.printMessage("\n\nSelect a book to view all users' progress:");
    books.forEach(book -> {
      view.printMessage("Book: " + book.getTitle() + ", Author: " + book.getAuthor() +
          ", Pages: " + book.getNumPages());
    });
    String userInput = view.getUserInput("\nEnter the book title to view progress: ");
    Book selectedBook = books.stream()
        .filter(book -> book.getTitle().equalsIgnoreCase(userInput))
        .findFirst()
        .orElse(null);
    if (selectedBook != null) {
      Response response = sendRequest(new Request("getAllUsersProgress", Map.of("book", selectedBook)));
      List<UserProgress> trackers = objectMapper.convertValue(
          response.getData().get("progressList"),
          new TypeReference<List<UserProgress>>() {
          });
      view.printMessage("\n\nProgress for book: " + selectedBook.getTitle());
      trackers.forEach(tracker -> {
        view.printMessage("User: " + tracker.getUsername() +
            ", Progress: " + tracker.getProgress() + " out of " + selectedBook.getNumPages() +
            ", Rating: " + (tracker.getRating() > 0 ? tracker.getRating() : "Not Rated"));
      });
    } else {
      view.printMessage("No book found with title: " + userInput);
    }
    showMainMenu();
  }

  private void showTrackers() {
    view.printMessage("\n\nDisplaying trackers for user: " + currentUser.getName());
    List<Tracker> allTrackers = getUserTrackers();
    List<Tracker> completedTrackers = allTrackers.stream()
        .filter(tracker -> tracker.getProgress() == tracker.getBook().getNumPages())
        .collect(Collectors.toList());
    List<Tracker> inProgressTrackers = allTrackers.stream()
        .filter(tracker -> tracker.getProgress() < tracker.getBook().getNumPages())
        .collect(Collectors.toList());
    List<Book> allBooks = getAllBooks();
    List<Book> untrackedBooks = allBooks.stream()
        .filter(book -> allTrackers.stream().noneMatch(tracker -> tracker.getBook().getBookId() == book.getBookId()))
        .collect(Collectors.toList());
    if (completedTrackers.size() > 0)
      view.printMessage("\nCompleted Books:");
    completedTrackers.forEach(t -> {
      view.printMessage(t.getBook().getTitle() +
          ", Rating: " + (t.getRating() > 0 ? t.getRating() : "Not Rated"));
    });
    if (inProgressTrackers.size() > 0)
      view.printMessage("\nBooks In Progress:");
    inProgressTrackers.forEach(t -> {
      view.printMessage(t.getBook().getTitle() +
          ", Progress: " + t.getProgress() + " out of " + t.getBook().getNumPages());
    });
    if (untrackedBooks.size() > 0)
      view.printMessage("\nUntracked Books:");
    untrackedBooks.forEach(book -> {
      view.printMessage("Book: " + book.getTitle() + ", Author: " + book.getAuthor() +
          ", Pages: " + book.getNumPages());
    });
    if (allTrackers.isEmpty()) {
      view.printMessage("You have no trackers, add to trackers to see progress here.");
    }
    showMainMenu();
  }

  private List<Tracker> getUserTrackers() {
    Response response = sendRequest(new Request("getUserProgress", Map.of("user", currentUser)));
    return objectMapper.convertValue(
        response.getData().get("progressMap"),
        new TypeReference<List<Tracker>>() {
        });
  }

  private List<Book> getAllBooks() {
    Response response = sendRequest(new Request("getAllBooks", Map.of()));
    return objectMapper.convertValue(
        response.getData().get("books"),
        new TypeReference<List<Book>>() {
        });
  }

  private void updateTrackerProgress() {
    List<Book> books = getAllBooks();
    List<Tracker> trackers = getUserTrackers();
    Set<Integer> trackedBookIds = trackers.stream()
        .map(tracker -> tracker.getBook().getBookId())
        .collect(Collectors.toSet());
    books.removeIf(book -> trackedBookIds.contains(book.getBookId()));
    view.printMessage("\n\nSelect a book to update progress:\n");
    view.printMessage("Tracked Books:");
    trackers.forEach(tracker -> {
      view.printMessage("Book: " + tracker.getBook().getTitle() +
          ", Progress: " + tracker.getProgress() + " out of " + tracker.getBook().getNumPages());
    });
    view.printMessage("\nUntracked Books:");
    books.forEach(book -> {
      view.printMessage("Book: " + book.getTitle() + ", Author: " + book.getAuthor() +
          ", Pages: " + book.getNumPages());
    });
    String userInput = view.getUserInput("\nEnter the book title to update progress: ");
    boolean updatingTracker = trackers.stream()
        .anyMatch(tracker -> tracker.getBook().getTitle().equalsIgnoreCase(userInput)
            && upsertTrackerProgress(tracker.getBook()));
    boolean updatingBooks = books.stream()
        .anyMatch(book -> book.getTitle().equalsIgnoreCase(userInput) && upsertTrackerProgress(book));
    if (!updatingTracker && !updatingBooks) {
      view.printMessage("There was an error updating progress for " + userInput);
      updateTrackerProgress();
    } else {
      showMainMenu();
    }
  }

  private boolean upsertTrackerProgress(Book book) {
    view.printMessage("\nUpdating progress for book: " + book.getTitle());
    int progress = Integer.parseInt(view.getUserInt("Enter your progress for this book: ", 0, book.getNumPages()));
    Response response = sendRequest(new Request("updateProgress", Map.of(
        "user", currentUser,
        "book", book,
        "progress", progress)));
    view.printMessage(response.getMessage());
    return response.getMessage().contains("success");
  }

}
