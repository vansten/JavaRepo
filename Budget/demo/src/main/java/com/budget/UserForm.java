package com.budget;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by janbe on 15.05.2017.
 */
public class UserForm {
    @NotNull
    @Size(min=2, max=30)
    private String firstName;

    @NotNull
    @Size(min=2, max=30)
    private String lastName;

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String toString() {
        return "User(First name: " + this.firstName + ", Last name: " + this.lastName + ")";
    }
}
