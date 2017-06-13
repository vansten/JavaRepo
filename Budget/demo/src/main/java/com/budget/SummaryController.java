package com.budget;

import com.budget.data.Entry;
import com.budget.data.User;
import com.budget.forms.UserForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Maverick on 09.06.2017.
 */
@Controller
public class SummaryController {

    @RequestMapping(value = "/in_out", method = RequestMethod.GET)
    public String showForm(Model model)
    {
        final String formName = "in_out";

        User loggedUser = AppController.getInstance().getLoggedUser();
        if(loggedUser == null)
            return formName;

//        List<List<Object>> rows = getTestRows();
        List<List<Object>> rows = getRows();
        List<String> headers = getHeaders();

        List<String> summaryHeaders = getSummaryHeaders();
        List<List<Object>> summaryRows = getSummaryRows(loggedUser);

        model.addAttribute("tableHeaders", headers);
        model.addAttribute("tableRows", rows);
        model.addAttribute("summaryHeaders", summaryHeaders);
        model.addAttribute("summaryRows", summaryRows);

        return formName;
    }

    private List<String> getSummaryHeaders() {
        List<String> headers = new ArrayList<>();
        headers.add("");
        headers.add("Expenses");
        headers.add("Earnings");
        return headers;
    }

    private List<List<Object>> getSummaryRows(User loggedUser) {
        ArrayList<List<Object>> rows = new ArrayList<>();
        DataSummary dataSummary = DataProcessor.calculateDataSummary(loggedUser.getExpenses(), loggedUser.getEarnings());

        ArrayList<Object> perDay = new ArrayList<>();
        perDay.add("Per day");
        perDay.add(dataSummary.getExpensesPerDay());
        perDay.add(dataSummary.getEarningsPerDay());

        ArrayList<Object> perMonth = new ArrayList<>();
        perMonth.add("Per month");
        perMonth.add(dataSummary.getExpensesPerMonth());
        perMonth.add(dataSummary.getEarningsPerMonth());

        ArrayList<Object> perYear = new ArrayList<>();
        perYear.add("Per year");
        perYear.add(dataSummary.getExpensesPerYear());
        perYear.add(dataSummary.getEarningsPerYear());

        rows.add(perDay);
        rows.add(perMonth);
        rows.add(perYear);
        return rows;
    }

    private List<String> getHeaders() {
        List<String> headers = new ArrayList<>();
        Entry.getFieldNames(headers);
        return headers;
    }

    private List<List<Object>> getTestRows() {
        List<List<Object>> rows = new ArrayList<>();

        for(int i = 0; i < 10; ++i) {
            List<Object> row = new ArrayList<>();
            row.add(i);
            row.add("Piwo");
            row.add(500.0f * (float)(i));
            row.add(Date.from(Instant.now()));
            row.add("Oblewanie wakacji");

            rows.add(row);
        }

        return rows;
    }

    private List<List<Object>> getRows() {
        List<List<Object>> rows = new ArrayList<>();
        ArrayList<Entry> entries =
                AppController.getInstance().getDbController().getEntriesForUser(
                        AppController.getInstance().getLoggedUser()
                );

        for(Entry e : entries) {
            List<Object> objs = new ArrayList<>();
            e.toList(objs);
            rows.add(objs);
        }

        return rows;
    }
}
