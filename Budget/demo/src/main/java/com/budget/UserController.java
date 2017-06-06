package com.budget;

import com.budget.data.User;
import com.budget.forms.LoginForm;
import com.budget.forms.RegisterForm;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

/**
 * Created by Krzysztof on 3/26/2017.
 */

@Controller
public class UserController {
//    @RequestMapping("/editUser/{id}")
//    public ModelAndView editUser(@PathVariable(value="id") String id) {
//        ModelAndView mav = new ModelAndView();
//        mav.setViewName("editUser");
//
//        return mav;
//    }

    @GetMapping("/register")
    public String showRegisterForm(RegisterForm registerForm) { return "registerForm"; }

    @PostMapping("/register")
    public String tryRegister(@Valid RegisterForm registerForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "redirect:register?isError=true";
        }

        User newUser = AppController.getInstance().getDbController().registerUser(registerForm.getLogin(), registerForm.getPassword());
        if(newUser == null || newUser.getID() == User.INVALID_ID) {
            return "redirect:register?isError=true";
        }
        AppController.getInstance().setLoggedUser(newUser);

        return "redirect:main?isLogged=true";
    }

    @GetMapping("/login")
    public String showLoginForm(LoginForm loginForm) { return "loginForm"; }

    @PostMapping("/login")
    public String tryLogin(@Valid LoginForm loginForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "redirect:login?isError=true";
        }

        User loggedUser = AppController.getInstance().getDbController().loginUser(loginForm.getLogin(), loginForm.getPassword());
        if(loggedUser == null || loggedUser.getID() == User.INVALID_ID) {
            return "redirect:login?isError=true";
        }
        AppController.getInstance().setLoggedUser(loggedUser);

        return "redirect:main?isLogged=true";
    }
}
