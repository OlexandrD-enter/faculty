package com.app.faculty.controller;

import com.app.faculty.model.Course;
import com.app.faculty.model.User;
import com.app.faculty.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String showCoursesPage(Model model) {
        return findPaginatedUsers(1, model);
    }

    @RequestMapping("/users/page/{pageNum}")
    public String findPaginatedUsers(@PathVariable(value = "pageNum") int pageNo, Model model) {
        int pageSize = 5;
        Page<User> page = userService.findPaginated(pageNo, pageSize);
        List<User> listUsers = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listUsers", listUsers);
        return "users";
    }

    @PutMapping("/users/{id}")
    public String disableUser(Model model, @PathVariable(value = "id") Long id) {
        userService.disableUserById(id);
        return "redirect:/admin/users/";
    }
}
