package com.cognixia.javafuturehorizons.capstone.view;

import java.util.Scanner;

import com.cognixia.javafuturehorizons.capstone.model.Response;

/*
 * View class for the Tracker application.
 * 
 * Presentation Logic
 * 
 * Displays UI, handles user input, and updates the Controller. 
 * 
 * Does not know what to do with the input, just that it occurred, and 
 * sends it to the Controller.
 * 
 * The view gets a Response from the Controller, and prints it.
 * The controller handles the formatting of the Response, including
 * converting it from JSON.
 */
public class View {

  private static View instance = null;
  private Scanner scanner;

  private View() {
    // Private constructor to prevent instantiation
    this.scanner = new Scanner(System.in);
  }

  public static View getInstance() {
    if (instance == null) {
      instance = new View();
    }
    return instance;
  }

  public void printResponse(Response response) {
    System.out.println(response.getMessage());
    response.getData().forEach((label, data) -> {
      System.out.println(label + ": " + data.toString());
    });
  }
  
  public void printMessage(String message) {
    System.out.println(message);
  }

  public String getUserInput(String prompt) {
    if (!prompt.isEmpty())
      System.out.print(prompt);
    return scanner.nextLine();
  }
  
  public String getUserInt(String prompt, int min, int max) {
    String input = getUserInput(prompt);
    try {
      Integer.parseInt(input);
      if (Integer.parseInt(input) < min || Integer.parseInt(input) > max) {
        System.out.println("Input must be between " + min + " and " + max + " (inclusive).");
        return getUserInt(prompt, min, max);
      }
      return input;
    } catch (NumberFormatException e) {
      System.out.println("Invalid input. Please enter a valid integer.");
      return getUserInt(prompt, min, max);
    }
  }

  public void printMenu(String[] options) {
    System.out.println("\n\nPlease select an option:");
    for (int i = 0; i < options.length; i++) {
      System.out.println((i + 1) + ". " + options[i]);
    }
  }

  public int getUserMenuChoice(String[] options) {
    printMenu(options);
    String choice = getUserInput("Enter your choice (1-" + options.length + "): ");
    try {
      int index = Integer.parseInt(choice);
      if (index < 1 || index > options.length) {
        System.out.println("Invalid choice. Please try again.");
        return getUserMenuChoice(options);
      }
      return index;
    } catch (NumberFormatException e) {
      System.out.println("Invalid input. Please enter a number.");
      return getUserMenuChoice(options);
    }
  }

}
