package com.budget;

import com.budget.data.Earning;
import com.budget.data.Expense;

import java.util.ArrayList;

/**
 * Created by krzys on 6/13/2017.
 */

public class DataProcessor
{
    public static DataSummary calculateDataSummary(ArrayList<Expense> expenses, ArrayList<Earning> earnings)
    {
        DataSummary dataSummary = new DataSummary();

        if(expenses != null) {
            dataSummary.addExpenses(expenses);
        }
        if(earnings != null) {
            dataSummary.addEarnings(earnings);
        }

        return dataSummary;
    }
}