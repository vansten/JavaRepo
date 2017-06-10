package com.budget;

import com.budget.forms.UserForm;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by Maverick on 09.06.2017.
 */
@Controller
public class SummaryController {

    @GetMapping("/in_out")
    public String showForm(UserForm userForm) {
        return "in_out";
    }


}
