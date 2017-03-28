package com.budget;

import com.budget.data.User;
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
        this.errorMessage = this.user != null ? "" : "Cannot registerUser user";
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
            path = "/registerUser",
            method = RequestMethod.GET )
    public RegisterUserResponse registerNewUser(@RequestParam(name="loginUser", required = true) String login, @RequestParam(name="pass", required = true)  String password) {
        //TODO:
        // Check password and loginUser
        User newUser = AppController.getInstance().getDbController().registerUser(login, password);
        return new RegisterUserResponse(newUser);
    }

    @RequestMapping("/loginUser")
    @ResponseBody
    public LoginUserResponse loginUser(@RequestParam(value="loginUser", required=true) String login, @RequestParam(value="pass", required=true) String password) {
        User loggedUser = AppController.getInstance().getDbController().loginUser(login, password);
        if(loggedUser == null || loggedUser.getID() == User.INVALID_ID) {
            return new LoginUserResponse("Invalid loginUser or password");
        }
        return new LoginUserResponse(loggedUser);
    }
}
