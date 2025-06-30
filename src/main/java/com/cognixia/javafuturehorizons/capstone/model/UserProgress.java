package com.cognixia.javafuturehorizons.capstone.model;

public class UserProgress {

  private String username;
  private int progress;
  private int rating;

  public UserProgress() {
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((username == null) ? 0 : username.hashCode());
    result = prime * result + progress;
    result = prime * result + rating;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    UserProgress other = (UserProgress) obj;
    if (username == null) {
      if (other.username != null)
        return false;
    } else if (!username.equals(other.username))
      return false;
    if (progress != other.progress)
      return false;
    if (rating != other.rating)
      return false;
    return true;
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
