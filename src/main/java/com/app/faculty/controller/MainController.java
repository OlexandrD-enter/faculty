package com.app.faculty.controller;

import com.app.faculty.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String homePage(@AuthenticationPrincipal UserDetailsImpl currentUser, Model model) {
        if (currentUser != null){
            model.addAttribute("name", currentUser.getUsername());
        }else {
            model.addAttribute("name", "No user has logged in");
        }
        return "index";
    }


}
