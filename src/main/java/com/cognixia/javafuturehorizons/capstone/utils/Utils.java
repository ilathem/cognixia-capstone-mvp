package com.cognixia.javafuturehorizons.capstone.utils;

import com.cognixia.javafuturehorizons.capstone.model.Request;
import com.cognixia.javafuturehorizons.capstone.model.Response;
import com.cognixia.javafuturehorizons.capstone.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {

    

  public static User getUserFromRequest(Request request) {
    Object userObj = request.getData().get("user");
    User newUser;
    if (userObj instanceof User) {
      newUser = (User) userObj;
    } else {
      newUser = new ObjectMapper().convertValue(userObj, User.class);
    }
    return newUser;
  }

  public static User getUserFromResponse(Response response) {
    Object userObj = response.getData().get("user");
    User newUser;
    if (userObj instanceof User) {
      newUser = (User) userObj;
    } else {
      newUser = new ObjectMapper().convertValue(userObj, User.class);
    }
    return newUser;
  }

}
