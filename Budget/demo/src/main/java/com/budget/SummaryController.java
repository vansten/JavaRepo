package com.budget;

import com.budget.data.Entry;
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

        if(AppController.getInstance().getLoggedUser() == null)
            return formName;

//        List<List<Object>> rows = getTestRows();
        List<List<Object>> rows = getRows();
        List<String> headers = getHeaders();

        model.addAttribute("tableHeaders", headers);
        model.addAttribute("tableRows", rows);

        return formName;
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
