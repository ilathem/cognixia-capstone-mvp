package com.cognixia.javafuturehorizons.capstone.model;

public class UserProgress {

  private String username;
  private int progress;
  private int rating;

  public UserProgress() {
  }
  
  public UserProgress(String username, int progress, int rating) {
    this.username = username;
    this.progress = progress;
    this.rating = rating;
  }

  public String getUsername() {
    return this.username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public int getProgress() {
    return this.progress;
  }

  public void setProgress(int progress) {
    this.progress = progress;
  }

  public int getRating() {
    return this.rating;
  }

  public void setRating(int rating) {
    this.rating = rating;
  }

}
