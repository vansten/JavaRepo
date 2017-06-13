package com.budget;

import com.budget.data.Earning;
import com.budget.data.Expense;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.ArrayList;

/**
 * Created by krzys on 6/13/2017.
 */
public class DataSummary
{
    private float expensesPerDay;
    private float earningsPerDay;
    private float expensesPerMonth;
    private float earningsPerMonth;
    private float expensesPerYear;
    private float earningsPerYear;

    public void addExpenses(ArrayList<Expense> expenses)
    {
        Instant now = Instant.now();
        Instant minDate = now;
        for (Expense e: expenses) {
            expensesPerDay += e.getValue();
            expensesPerMonth += e.getValue();
            expensesPerYear += e.getValue();

            Instant expenseDate = e.getTimestamp();
            if(expenseDate.isBefore(minDate)) {
                minDate = expenseDate;
            }
        }

        long days = ChronoUnit.DAYS.between(minDate.atZone(ZoneId.systemDefault()).toLocalDate(), now.atZone(ZoneId.systemDefault()).toLocalDate()) + 1;
        long months = ChronoUnit.MONTHS.between(minDate.atZone(ZoneId.systemDefault()).toLocalDate(), now.atZone(ZoneId.systemDefault()).toLocalDate()) + 1;
        long years = ChronoUnit.YEARS.between(minDate.atZone(ZoneId.systemDefault()).toLocalDate(), now.atZone(ZoneId.systemDefault()).toLocalDate()) + 1;

        expensesPerDay /= (float)days;
        expensesPerMonth /= (float)months;
        expensesPerYear /= (float)years;
    }

    public void addEarnings(ArrayList<Earning> earnings)
    {Instant now = Instant.now();
        Instant minDate = now;
        for (Earning e: earnings) {
            earningsPerDay += e.getValue();
            earningsPerMonth += e.getValue();
            earningsPerYear += e.getValue();

            Instant earningTime = e.getTimestamp();
            if(earningTime.isBefore(minDate)) {
                minDate = earningTime;
            }
        }

        long days = ChronoUnit.DAYS.between(minDate.atZone(ZoneId.systemDefault()).toLocalDate(), now.atZone(ZoneId.systemDefault()).toLocalDate()) + 1;
        long months = ChronoUnit.MONTHS.between(minDate.atZone(ZoneId.systemDefault()).toLocalDate(), now.atZone(ZoneId.systemDefault()).toLocalDate()) + 1;
        long years = ChronoUnit.YEARS.between(minDate.atZone(ZoneId.systemDefault()).toLocalDate(), now.atZone(ZoneId.systemDefault()).toLocalDate()) + 1;

        earningsPerDay /= (float)days;
        earningsPerMonth /= (float)months;
        earningsPerYear /= (float)years;
    }

    public float getExpensesPerDay() {
        return expensesPerDay;
    }

    public float getEarningsPerDay() {
        return earningsPerDay;
    }

    public float getExpensesPerMonth() {
        return expensesPerMonth;
    }

    public float getEarningsPerMonth() {
        return earningsPerMonth;
    }

    public float getExpensesPerYear() {
        return expensesPerYear;
    }

    public float getEarningsPerYear() {
        return earningsPerYear;
    }
}
