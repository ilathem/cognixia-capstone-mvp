package com.cognixia.javafuturehorizons.capstone.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
  private static final String URL = "jdbc:mysql://localhost:3306/tracker_db";
  private static final String USERNAME = "root";
  private static final String PASSWORD = "root"; // change password if needed, Mac/Linux = Root@123 | Windows = root

  private static Connection connection = null;

  private static void makeConnection() throws ClassNotFoundException, SQLException {

    Class.forName("com.mysql.cj.jdbc.Driver");

    connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

  }

  public static Connection getConnection() throws ClassNotFoundException, SQLException {

    if (connection == null) {
      makeConnection();
    }

    return connection;
  }

  public static void main(String[] args) {

    Connection conn = null;

    try {
      conn = ConnectionManager.getConnection();
      System.out.println("Connected");

    } catch (ClassNotFoundException e1) {
      e1.printStackTrace();
    } catch (SQLException e1) {
      e1.printStackTrace();
    }

    try {
      conn.close();
      System.out.println("Closed connection");

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
