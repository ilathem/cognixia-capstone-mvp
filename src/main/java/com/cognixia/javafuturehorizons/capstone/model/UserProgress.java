package com.cognixia.javafuturehorizons.capstone.model;

public class UserProgress {

  private String username;
  private int progress;

  public UserProgress() {
  }
  
  public UserProgress(String username, int progress) {
    this.username = username;
    this.progress = progress;
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


}
