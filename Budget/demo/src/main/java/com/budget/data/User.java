package com.budget.data;

import java.util.ArrayList;

/**
 * Created by Krzysztof on 3/26/2017.
 */
public class User {
    public static Integer INVALID_ID = -1;

    protected Integer id;
    protected String login;
    protected String password;
    protected String firstName;
    protected String lastName;
    protected ArrayList<String> notifications;


    protected User(Integer id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return this.login;
    }

    public Integer getID() {
        return this.id;
    }
}
