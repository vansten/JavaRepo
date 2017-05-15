package com.budget;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

/**
 * Created by janbe on 15.05.2017.
 */
@Controller
public class NotificationController {

    @GetMapping("/notifications")
    public ModelAndView showNotifications(){
        //GET NOTIFICATIONS FROM USER

        ArrayList<String> notifications = new ArrayList<String>();

        notifications.add("Simple Notification");
        ModelAndView mv = new ModelAndView();
        mv.addObject("notifications", notifications);
        mv.setViewName("notifications");
        return mv;
    }
}
