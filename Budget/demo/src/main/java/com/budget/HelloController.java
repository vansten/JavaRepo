package com.budget;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by janbe on 13.03.2017.
 */
@RestController
public class HelloController {

    @RequestMapping("/greeting")
    public ModelAndView greeting(@RequestParam(value="name", required=false, defaultValue="World") String name) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("greeting");

        mav.addObject("name", name);
        return mav;
    }
}
