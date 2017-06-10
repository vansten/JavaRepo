package com.budget;

import javax.validation.Valid;

import com.budget.forms.LoginForm;
import com.budget.forms.UserForm;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by janbe on 15.05.2017.
 */
@Controller
public class WebController extends WebMvcConfigurerAdapter {

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

//    @Override
//    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
//    }
}
