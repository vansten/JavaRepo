package com.budget.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by krzys on 6/6/2017.
 */
public class RegisterForm {
    @NotNull
    @Size(min = 6)
    private String login;

    @NotNull
    @Size(min = 8)
    private String password;

    public String getLogin() { return login; }
    public String getPassword() { return password; }

    public void setLogin(String login) { this.login = login; }
    public void setPassword(String password) { this.password = password; }
}
