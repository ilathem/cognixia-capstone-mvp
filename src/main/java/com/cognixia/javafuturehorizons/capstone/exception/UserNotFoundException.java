package com.cognixia.javafuturehorizons.capstone.exception;

public class UserNotFoundException extends Exception {

    public UserNotFoundException(String username) {
        super("The user '" + username + "' does not exist.");
    }

}
