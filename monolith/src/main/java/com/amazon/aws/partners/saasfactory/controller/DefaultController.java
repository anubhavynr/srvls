package com.amazon.aws.partners.saasfactory.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@ControllerAdvice("com.amazon.aws.partners.saasfactory.controller")
public class DefaultController {

    private final static Logger logger = LoggerFactory.getLogger(DefaultController.class);

    @ModelAttribute("username")
    public String username() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @ModelAttribute("isAuthenticated")
    public boolean isAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
    }

    @RequestMapping(value = {"/", "/index.html"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model, @RequestParam(required = false) String error) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        return "index";
    }

}
