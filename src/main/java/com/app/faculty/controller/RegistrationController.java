package com.app.faculty.controller;

import com.app.faculty.dto.UserDTO;
import com.app.faculty.model.Role;
import com.app.faculty.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import javax.validation.Valid;

@Controller
@RequestMapping("/registration")
public class RegistrationController {
    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService){
        this.userService = userService;
    }

    @GetMapping()
    public String registration(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            model.addAttribute("userForm", new UserDTO());
            return "registration";
        }
        return "redirect:/";
    }

    @PostMapping()
    public ModelAndView addUser(@ModelAttribute("userForm") @Valid UserDTO userDTO, BindingResult result, ModelAndView modelAndView) {
        if (result.hasErrors()) {
            modelAndView.addObject("failureMessage", "signup.label.error");
        } else {
            if (userService.findByEmail(userDTO.getEmail()).isPresent()) {
                modelAndView.addObject("failureMessage", "signup.label.emailRegistered");
                modelAndView.setViewName("registration");
            } else if (userService.findUserByUserName(userDTO.getUsername()).isPresent()) {
                modelAndView.addObject("failureMessage", "signup.label.alreadyRegistered");
                modelAndView.setViewName("registration");
            } else if (!userDTO.getPassword().equals(userDTO.getPasswordConfirm())) {
                modelAndView.addObject("failureMessage", "signup.label.passwords");
                modelAndView.setViewName("registration");
            } else {
                userService.saveUser(userDTO, Role.ROLE_USER);
                modelAndView.setViewName("redirect:/registration?success");
            }
        }
        return modelAndView;
    }
}
