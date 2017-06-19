package com.budget;

import com.budget.data.Entry;
import com.budget.data.User;
import com.budget.forms.FilterForm;
import com.budget.forms.LoginForm;
import com.budget.forms.UserForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Maverick on 09.06.2017.
 */
@Controller
public class SummaryController {

    private static SummaryController instance = null;

    private FilterUtility filter;
    private ChartUtility chart;
    private User prevLoggedUser = null;
    private boolean userChangedEntries = false;

    @PostConstruct
    public void Construct() {
        filter = new FilterUtility();
        chart = new ChartUtility();

        instance = this;
    }

    public SummaryController getInstance() { assert(instance != null); return instance; }


    @RequestMapping(value = "/in_out", method = RequestMethod.GET)
    public String showForm(Model model, FilterForm filterForm)
    {
        final String formName = "in_out";

        User loggedUser = AppController.getInstance().getLoggedUser();
        if(loggedUser == null)
            return formName;

        boolean userChanged = loggedUser != prevLoggedUser;
        if(userChanged)
            prevLoggedUser = loggedUser;

//        List<List<Object>> rows = getTestRows();
        List<List<Object>> rows = getRows();
        List<String> headers = getHeaders();

        List<String> summaryHeaders = getSummaryHeaders();
        List<List<Object>> summaryRows = getSummaryRows(loggedUser);

        model.addAttribute("tableHeaders", headers);
        model.addAttribute("tableRows", rows);
        model.addAttribute("summaryHeaders", summaryHeaders);
        model.addAttribute("summaryRows", summaryRows);

        filter.updateFromUserEntries(userChanged || userChangedEntries);

        if(userChangedEntries)
            userChangedEntries = false;

        // apply filters to filter form
        filterForm.setValueMin(filter.FilterValue.MinValue);
        filterForm.setValueMax(filter.FilterValue.MaxValue);
        filterForm.setDateMinAsLocalDate(filter.getDateMin());
        filterForm.setDateMaxAsLocalDate(filter.getDateMax());

        // update chart data
        chart.generateDatasets(loggedUser, filter);

        return formName;
    }

    @RequestMapping(value = "/chartValueDate", method = RequestMethod.GET)
    public void drawChartValueDate(HttpServletResponse response) {
        chart.drawToResponseValueDate(response);
    }

    @RequestMapping(value = "/chartNames", method = RequestMethod.GET)
    public void drawChartNames(HttpServletResponse response) {
        chart.drawToResponseNames(response);
    }

    @RequestMapping(value = "/chartSources", method = RequestMethod.GET)
    public void drawChartSources(HttpServletResponse response) {
        chart.drawToResponseSources(response);
    }

    @PostMapping("/filter")
    public String tryFilter(@Valid FilterForm filterForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "redirect:in_out?isError=true";
        }

        // apply read filters
        filter.FilterValue.MinValue = filterForm.getValueMin();
        filter.FilterValue.MaxValue = filterForm.getValueMax();
        filter.setDateMin(filterForm.getDateMinAsLocalDate());
        filter.setDateMax(filterForm.getDateMaxAsLocalDate());

        return "redirect:in_out";
    }

    public void notifyUserChangedEntries() {
        userChangedEntries = true;
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

    private List<List<Object>> getRows() {
        List<List<Object>> rows = new ArrayList<>();
        ArrayList<Entry> entries =
                AppController.getInstance().getDbController().getEntriesForUser(
                        AppController.getInstance().getLoggedUser()
                );

        for(Entry e : entries) {

            if(!filter.isEntryPassingFilters(e))
                continue;

            List<Object> objs = new ArrayList<>();
            e.toList(objs);
            rows.add(objs);
        }

        return rows;
    }
}
