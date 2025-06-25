package com.cognixia.javafuturehorizons.capstone.model;

import java.util.Map;

public class Response {

  private String message;
  private Map<String, Object> data;

  public Response() {
  }
  
  public Response(String message) {
    this.message = message;
    this.data = null; // Default to null if no data is provided
  }

  public Response(Map<String, Object> data) {
    this.data = data;
    this.message = "Operation successful";
  }

  public Response(String message, Map<String, Object> data) {
    this.message = message;
    this.data = data;
  }

  public String getMessage() {
    return this.message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Map<String, Object> getData() {
    return this.data;
  }

  public void setData(Map<String, Object> data) {
    this.data = data;
  }

}
