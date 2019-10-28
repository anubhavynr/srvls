package com.amazon.aws.partners.saasfactory.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
    
    @ModelAttribute("isAnonymous")
    public boolean isAnonymous() {
        return SecurityContextHolder.getContext().getAuthentication().getName() == "anonymousUser";
    }

    @RequestMapping(value = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model, @RequestParam(required = false) String error) {
        String returnView;
        
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        
        return "index";
    }

}
