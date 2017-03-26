package com.budget;

import org.springframework.web.bind.annotation.*;

/**
 * Created by Krzysztof on 3/26/2017.
 */

enum ResponseType {
    SUCCESS,
    FAILURE
}

class RegisterUserResponse {
    public User user;
    public String errorMessage;
    public ResponseType responseType;

    public RegisterUserResponse(User user) {
        this.user = user;
        this.responseType = this.user != null ? ResponseType.SUCCESS : ResponseType.FAILURE;
        this.errorMessage = this.user != null ? "" : "Cannot register user";
    }

    public RegisterUserResponse(String errorMessage) {
        this.user = null;
        this.responseType = ResponseType.FAILURE;
        this.errorMessage = errorMessage;
    }
}

class LoginUserResponse {
    public User user;
    public String errorMessage;
    public ResponseType responseType;

    public LoginUserResponse(User user) {
        this.user = user;
        this.responseType = this.user != null ? ResponseType.SUCCESS : ResponseType.FAILURE;
        this.errorMessage = this.user != null ? "" : "Cannot log user in";
    }

    public LoginUserResponse(String errorMessage) {
        this.user = null;
        this.responseType = ResponseType.FAILURE;
        this.errorMessage = errorMessage;
    }
}

@RestController
public class UserController {
    @RequestMapping(
            path = "/register",
            method = RequestMethod.GET )
    public RegisterUserResponse registerNewUser(@RequestParam(name="login", required = true) String login, @RequestParam(name="pass", required = true)  String password) {
        //TODO:
        // Check password and login
        Integer id = 0; //Generate new id from DB
        User newUser = new User(id, login, password);
        return new RegisterUserResponse(newUser);
    }

    @RequestMapping("/login")
    @ResponseBody
    public LoginUserResponse loginUser(@RequestParam(value="login", required=true) String login, @RequestParam(value="pass", required=true) String password) {
        Integer id = 1; //Get id from existing record in DB
        if(id == User.INVALID_ID) {
            return new LoginUserResponse("Invalid login or password");
        }
        User loggedUser = new User(id, login, password);
        return new LoginUserResponse(loggedUser);
    }
}
