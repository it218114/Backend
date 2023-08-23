package com.erasmus.appli.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ViewController {
	
	@RequestMapping("/register")
    public ModelAndView showRegisterPage() {
		return new ModelAndView("register");
    }
	
	@RequestMapping(path = {"/login","/"})
    public String showMustGoOn(Model theModel){
        return "login";
    }
	
	@RequestMapping("/header")
    public ModelAndView showHeaderPage() {
		return new ModelAndView("header.html");
    }
	
	@RequestMapping("/users")
    public String showUserPage(Model theModel) {
		return "users";
    }
	
	@RequestMapping("/application")
    public String showAppPage(Model theModel) {
		return "application";
    }
}
