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

    private FilterUtility filter = new FilterUtility();


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
