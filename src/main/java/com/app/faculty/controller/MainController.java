package com.app.faculty.controller;

import com.app.faculty.model.Course;
import com.app.faculty.security.UserDetailsImpl;
import com.app.faculty.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/")
public class MainController {

    private final CourseService courseService;

    @Autowired
    public MainController(CourseService courseService){
        this.courseService = courseService;
    }

    @GetMapping()
    public String homePage(Model model) {
        List<Course> courseList = courseService.findAll();
        model.addAttribute("courseList", courseList);
        return "index";
    }
}
