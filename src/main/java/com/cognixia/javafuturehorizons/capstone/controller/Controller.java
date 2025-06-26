package com.cognixia.javafuturehorizons.capstone.controller;

import java.util.List;
import java.util.Map;

import com.cognixia.javafuturehorizons.capstone.model.Book;
import com.cognixia.javafuturehorizons.capstone.model.Model;
import com.cognixia.javafuturehorizons.capstone.model.Request;
import com.cognixia.javafuturehorizons.capstone.model.Response;
import com.cognixia.javafuturehorizons.capstone.model.Tracker;
import com.cognixia.javafuturehorizons.capstone.model.User;
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
    });
    switch (menuChoice) {
      case 1:
        showTrackers();
        break;
    
      default:
        break;
    }
  }

  private void showTrackers() {
    view.printMessage("Displaying trackers for user: " + currentUser.getName());
    Response response = sendRequest(new Request("getUserProgress", Map.of("user", currentUser)));
    List<Tracker> trackers = objectMapper.convertValue(
      response.getData().get("progressMap"),
      new TypeReference<List<Tracker>>() {}
    );
    trackers.forEach(tracker -> {
      view.printMessage("Book: " + tracker.getBook().getTitle() + 
        ", Progress: " + tracker.getProgress() + " out of " + tracker.getBook().getNumPages());
    });
    showMainMenu();
  }

}
