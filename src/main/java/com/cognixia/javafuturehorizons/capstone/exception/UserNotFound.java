package com.cognixia.javafuturehorizons.capstone.exception;

public class UserNotFound extends Exception {

    public UserNotFound(String username) {
        super("The user '" + username + "' does not exist.");
    }

}
