package com.budget;

import javax.validation.Valid;

import com.budget.forms.LoginForm;
import com.budget.forms.UserForm;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by janbe on 15.05.2017.
 */
@Controller
public class WebController extends WebMvcConfigurerAdapter {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //Probably those routes will be moved f.e. to proper controllers for counting incomes etc.
        registry.addViewController("/in_out").setViewName("in_out");
        registry.addViewController("/graphs").setViewName("graphs");
        //registry.addViewController("/notifications").setViewName("notifications");
    }

    @GetMapping("/editUser")
    public String showForm(UserForm userForm) {
        return "form";
    }

    @PostMapping("/editUser")
    public String checkUserInfo(@Valid UserForm userForm, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return "form";
        }

        return "redirect:/main?isLogged=true";
    }
}
