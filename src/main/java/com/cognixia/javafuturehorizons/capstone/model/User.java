package com.cognixia.javafuturehorizons.capstone.model;

public class User {
  private int userId;
  private int clearance;
  private String name;
  private String password;

  public User(int clearance, String name, String password) {
    this.clearance = clearance;
    this.name = name;
    this.password = password;
  }

  public User(int userId, int clearance, String name, String password) {
    this.userId = userId;
    this.clearance = clearance;
    this.name = name;
    this.password = password;
  }
  
  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public int getClearance() {
    return clearance;
  }

  public void setClearance(int clearance) {
    this.clearance = clearance;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String toString() {
    return "User{" +
        "userId=" + userId +
        ", clearance=" + clearance +
        ", name='" + name + '\'' +
        ", password='" + password + '\'' +
        '}';
  }
}
