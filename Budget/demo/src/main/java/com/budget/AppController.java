package com.budget;

import com.budget.data.DatabaseController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by janbe on 13.03.2017.
 */
@RestController
public class AppController {

    private static AppController instance;

    private DatabaseController dbController = new DatabaseController();

    @RequestMapping("/greeting")
    public ModelAndView greeting(@RequestParam(value="name", required=false, defaultValue="World") String name) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("greeting");

        // don't know where to put it, so I will put this here, hoping it will be called
        instance = this;
        dbController.initialize();  // need to shutdown it somewhere as well

        mav.addObject("name", name);
        return mav;
    }

    public static AppController getInstance() {
        if(instance == null) {
            instance = new AppController();
        }
        return instance;
    }

    public DatabaseController getDbController() {
        return dbController;
    }
}
