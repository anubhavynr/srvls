package com.amazon.aws.partners.saasfactory.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    @PostMapping("/login")
    public ModelAndView login() throws Exception {
        return null;
    }
}
