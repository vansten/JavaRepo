package com.budget.data;

import java.lang.reflect.Array;
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

    protected ArrayList<Expense> expenses = new ArrayList<Expense>();
    protected ArrayList<Earning> earnings = new ArrayList<Earning>();

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

    public void addExpense(Expense expense) { expenses.add(expense); }
    public void addEarning(Earning earning) { earnings.add(earning); }
    public void addExpenses(ArrayList<Expense> expenses) { this.expenses.addAll(expenses); }
    public void addEarnings(ArrayList<Earning> earnings) { this.earnings.addAll(earnings); }
    public void removeExpense(Expense expense) { expenses.remove(expense); }
    public void removeEarning(Earning earning) { earnings.remove(earning); }
    public ArrayList<Earning> getEarnings() { return earnings; }
    public ArrayList<Expense> getExpenses() { return expenses; }
}
