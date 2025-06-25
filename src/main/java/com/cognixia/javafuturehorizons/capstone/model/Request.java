package com.cognixia.javafuturehorizons.capstone.model;

import java.util.Map;

public class Request {

  private String action;
  private Map<String, Object> data;

  public Request() {
  }

  public Request(String action, Map<String, Object> data) {
    this.action = action;
    this.data = data;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public Map<String, Object> getData() {
    return data;
  }

  public void setData(Map<String, Object> data) {
    this.data = data;
  }

}
