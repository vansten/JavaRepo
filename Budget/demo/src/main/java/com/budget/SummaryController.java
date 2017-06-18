package com.budget;

import com.budget.data.Earning;
import com.budget.data.Entry;
import com.budget.data.Expense;
import com.budget.data.User;
import com.ritaja.xchangerate.api.CurrencyConverter;
import com.ritaja.xchangerate.api.CurrencyConverterBuilder;
import com.ritaja.xchangerate.util.Currency;
import com.ritaja.xchangerate.util.Strategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maverick on 09.06.2017.
 */
@Controller
public class SummaryController {

    private FilterUtility filter = new FilterUtility();


    @RequestMapping(value = "/in_out", method = RequestMethod.GET)
    public String showForm(Model model,
                           @RequestParam(value="currency", required=false) String currency)
    {
        final String formName = "in_out";

        User loggedUser = AppController.getInstance().getLoggedUser();
        if(loggedUser == null)
            return formName;

//        List<List<Object>> rows = getTestRows();
        List<List<Object>> rows = getRows(currency);
        List<String> headers = getHeaders();

        List<String> summaryHeaders = getSummaryHeaders();
        List<List<Object>> summaryRows = getSummaryRows(loggedUser, currency);

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

    private List<List<Object>> getSummaryRows(User loggedUser, String currency) {
        ArrayList<List<Object>> rows = new ArrayList<>();
        DataSummary dataSummary;

        if(currency != null){
            ArrayList<Expense> convertedExpenses = convertExpenses(loggedUser.getExpenses(), currency);
            ArrayList<Earning> convertedEarnings = convertEarnings(loggedUser.getEarnings(), currency);

            dataSummary = DataProcessor.calculateDataSummary(convertedExpenses, convertedEarnings);
        }
        else {
            dataSummary = DataProcessor.calculateDataSummary(loggedUser.getExpenses(), loggedUser.getEarnings());
        }

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

    private List<List<Object>> getRows(String currency) {
        List<List<Object>> rows = new ArrayList<>();
        ArrayList<Entry> entries =
                AppController.getInstance().getDbController().getEntriesForUser(
                        AppController.getInstance().getLoggedUser()
                );

        for(Entry e : entries) {

            if(!filter.isEntryPassingFilters(e))
                continue;

            List<Object> objs = new ArrayList<>();
            if(currency != null)
            {
                Entry converted = convertSingleEntry(e, currency);
                converted.toList(objs);
            }
            else {
                e.toList(objs);
            }
            rows.add(objs);
        }

        return rows;
    }

    private ArrayList<Expense> convertExpenses(ArrayList<Expense> expenses, String currency) {
        CurrencyConverter converter = new CurrencyConverterBuilder()
                .strategy(Strategy.YAHOO_FINANCE_FILESTORE)
                .buildConverter();

        converter.setRefreshRateSeconds(86400);

        ArrayList<Expense> outputArray = new ArrayList<>();

        expenses.forEach(entry -> {
            BigDecimal entryValue = BigDecimal.valueOf(entry.getValueAbsolute());
            Float converted = 0f;
            try {
                BigDecimal bd = converter.convertCurrency(entryValue, Currency.PLN,
                        Currency.valueOf(currency));
                converted = round(bd.floatValue(), 2);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            outputArray.add(new Expense(entry.getID(), converted, entry.getUserID(), entry.getTimestamp(),
                    entry.getName(), entry.getSourceName()));
        });

        return outputArray;
    }

    private ArrayList<Earning> convertEarnings(ArrayList<Earning> earnings, String currency) {
        CurrencyConverter converter = new CurrencyConverterBuilder()
                .strategy(Strategy.YAHOO_FINANCE_FILESTORE)
                .buildConverter();

        converter.setRefreshRateSeconds(86400);

        ArrayList<Earning> outputArray = new ArrayList<>();

        earnings.forEach(entry -> {
            BigDecimal entryValue = BigDecimal.valueOf(entry.getValueAbsolute());
            Float converted = 0f;
            try {
                BigDecimal bd = converter.convertCurrency(entryValue, Currency.PLN,
                        Currency.valueOf(currency));
                converted = round(bd.floatValue(), 2);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            outputArray.add(new Earning(entry.getID(), converted, entry.getUserID(), entry.getTimestamp(),
                    entry.getName(), entry.getSourceName()));
        });

        return outputArray;
    }

    private Entry convertSingleEntry(Entry e, String currency) {
        CurrencyConverter converter = new CurrencyConverterBuilder()
                .strategy(Strategy.YAHOO_FINANCE_FILESTORE)
                .buildConverter();

        converter.setRefreshRateSeconds(86400);

        BigDecimal entryValue = BigDecimal.valueOf(e.getValueAbsolute());
        Float converted = 0f;
        try {
            BigDecimal bd = converter.convertCurrency(entryValue, Currency.PLN,
                    Currency.valueOf(currency));
            converted = round(bd.floatValue(), 2);
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }

        return new Entry(e.getID(), converted, e.getUserID(), e.getTimestamp(),
                e.getName(), e.getSourceName());
    }

    private float round(float number, int scale) {
        int pow = 10;
        for (int i = 1; i < scale; i++)
            pow *= 10;
        float tmp = number * pow;
        return (float) (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) / pow;
    }
}
