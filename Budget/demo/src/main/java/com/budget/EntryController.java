package com.budget;

import com.budget.data.DatabaseController;
import com.budget.data.Earning;
import com.budget.data.Expense;
import com.budget.data.User;
import com.budget.forms.EntryForm;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

/**
 * Created by krzys on 6/6/2017.
 */
@Controller
public class EntryController
{
    @GetMapping("/incomes")
    public String showIncomes(EntryForm entryForm)
    {
        return "incomes";
    }

    @GetMapping("/outcomes")
    public String showOutcomes(EntryForm entryForm)
    {
        return "outcomes";
    }

    @PostMapping("/incomes")
    public String checkIncomeInfo(@Valid EntryForm entryForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
        {
            return "redirect:incomes?successful=false";
        }

        DatabaseController dbController = AppController.getInstance().getDbController();
        User loggedUser = AppController.getInstance().getLoggedUser();
        if(loggedUser == null)
        {
            return "redirect:incomes?successful=false";
        }
        Earning e = dbController.createEarning(loggedUser, entryForm.getValue().floatValue(), entryForm.getName(), entryForm.getSourceName());
        loggedUser.addEarning(e);

        return "redirect:/incomes?succesful=true";
    }

    @PostMapping("/outcomes")
    public String checkOutcomeInfo(@Valid EntryForm entryForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
        {
            return "redirect:outcomes?successful=false";
        }

        DatabaseController dbController = AppController.getInstance().getDbController();
        User loggedUser = AppController.getInstance().getLoggedUser();
        if(loggedUser == null)
        {
            return "redirect:outcomes?successful=false";
        }
        Expense e = dbController.createExpense(loggedUser, entryForm.getValue().floatValue(), entryForm.getName(), entryForm.getSourceName());
        loggedUser.addExpense(e);

        return "redirect:/outcomes?successful=true";
    }
}
