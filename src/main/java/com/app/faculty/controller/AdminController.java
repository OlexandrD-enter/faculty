package com.app.faculty.controller;

import com.app.faculty.dto.CourseDTO;
import com.app.faculty.dto.UserDTO;
import com.app.faculty.model.Course;
import com.app.faculty.model.Role;
import com.app.faculty.model.User;
import com.app.faculty.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/users")
    public String showUsersPage(Model model) {
        return findPaginatedUsers(1, model);
    }

    @RequestMapping("/users/page/{pageNum}")
    public String findPaginatedUsers(@PathVariable(value = "pageNum") int pageNo, Model model) {
        int pageSize = 5;
        Page<User> page = userService.findPaginated(pageNo, pageSize, Role.ROLE_USER);
        List<User> listUsers = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listUsers", listUsers);
        return "users";
    }

    @PutMapping("/users/{id}")
    public String disableUser(@PathVariable(value = "id") Long id) {
        userService.disableUserById(id);
        return "redirect:/admin/users/";
    }

    @GetMapping("/lecturers")
    public String showLecturersPage(Model model){
        return findPaginatedLecturers(1, model);
    }

    @RequestMapping("/lecturers/page/{pageNum}")
    public String findPaginatedLecturers(@PathVariable(value = "pageNum") int pageNo, Model model) {
        int pageSize = 5;
        Page<User> page = userService.findPaginated(pageNo, pageSize, Role.ROLE_LECTURER);
        List<User> listLecturers = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listLecturers", listLecturers);
        return "lecturers";
    }

    @GetMapping("/lecturers/create")
    public String showLecturerPage(Model model) {
        model.addAttribute("lecturerForm", new UserDTO());
        return "createLecturer";
    }

    @PostMapping("/lecturers/create")
    public ModelAndView registerNewLecturer(@ModelAttribute("lecturerForm") @Valid UserDTO userDTO,
                                        BindingResult result, ModelAndView modelAndView) {
        if (result.hasErrors()) {
            modelAndView.addObject("failureMessage", "signup.label.error");
        } else {
            if (userService.findByEmail(userDTO.getEmail()).isPresent()) {
                modelAndView.addObject("failureMessage", "signup.label.emailRegistered");
                modelAndView.setViewName("createLecturer");
            } else if (userService.findUserByUserName(userDTO.getUsername()) != null) {
                modelAndView.addObject("failureMessage", "signup.label.alreadyRegistered");
                modelAndView.setViewName("createLecturer");
            } else if (!userDTO.getPassword().equals(userDTO.getPasswordConfirm())) {
                modelAndView.addObject("failureMessage", "signup.label.passwords");
                modelAndView.setViewName("createLecturer");
            } else {
                userService.saveUser(userDTO, Role.ROLE_LECTURER);
                modelAndView.setViewName("redirect:/admin/lecturers/create?success");
            }
        }
        return modelAndView;
    }

    @DeleteMapping("/lecturers/{id}")
    public String deleteLecturer(@PathVariable(value = "id") Long id) {
        userService.deleteUserById(id);
        return "redirect:/admin/lecturers/";
    }

    @GetMapping("/lecturers/{id}")
    public String updateLecturerPage(Model model, @PathVariable Long id) {
        User lecturer = userService.findUserById(id);
        model.addAttribute("lecturer", lecturer);
        return "updateLecturer";
    }

    @PutMapping("/lecturers/{id}")
    public String saveCourse(@ModelAttribute("lecturer") UserDTO userDTO,
                             BindingResult bindingResult, @PathVariable Long id) {
        if (bindingResult.hasErrors()) {
            return "updateLecturer";
        }
        User lecturer = userService.findUserById(id);
        lecturer.setFirstName(userDTO.getFirstName());
        lecturer.setLastName(userDTO.getLastName());
        lecturer.setEmail(userDTO.getEmail());
        lecturer.setUsername(userDTO.getUsername());
        userService.updateUser(lecturer);
        return "redirect:/admin/lecturers/{id}?success";
    }
}
