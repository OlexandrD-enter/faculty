package com.app.faculty.controller;

import com.app.faculty.dto.CourseDTO;
import com.app.faculty.model.Course;
import com.app.faculty.model.Role;
import com.app.faculty.model.User;
import com.app.faculty.service.CourseService;
import com.app.faculty.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/courses")
public class MainController {

    private final CourseService courseService;
    private final UserService userService;

    @Autowired
    public MainController(CourseService courseService, UserService userService) {
        this.courseService = courseService;
        this.userService = userService;
    }

    @GetMapping()
    public String showCoursesPage(Model model) {
        List<Course> courseList = courseService.findAll();
        model.addAttribute("courseList", courseList);
        return "index";
    }

    @PostMapping()
    public String enrollAtCourse(@RequestParam("coursesId") Long courseId) {
        System.out.println(courseId);
        return "redirect:/courses";
    }

    @DeleteMapping("{id}")
    public String deleteCourseById(@PathVariable Long id) {
        courseService.deleteById(id);
        return "redirect:/courses";
    }

    @GetMapping("{id}")
    public String updateCoursePage(Model model, @PathVariable Long id) {
        Course course = courseService.findCourseById(id);
        List<User> lecturerList = userService.findUsersByRoleType(Role.ROLE_LECTURER);
        model.addAttribute("course", course);
        model.addAttribute("lecturerList", lecturerList);
        return "updateCourse";
    }

    @PutMapping("{id}")
    public String saveCourse(@ModelAttribute("course") @Valid CourseDTO courseDTO,
                             BindingResult bindingResult, @PathVariable Long id, Model model) {
        model.addAttribute("lecturerList", userService.findUsersByRoleType(Role.ROLE_LECTURER));
        if (bindingResult.hasErrors()) {
            return "updateCourse";
        }
        courseService.saveCourse(courseDTO);
        return "redirect:/courses/{id}?success";
    }

    @GetMapping("/create")
    public String addCourse(Model model) {
        model.addAttribute("courseForm", new CourseDTO());
        model.addAttribute("lecturerList", userService.findUsersByRoleType(Role.ROLE_LECTURER));
        return "createCourse";
    }

    @PostMapping("/create")
    public String addCourse(@ModelAttribute("courseForm") @Valid CourseDTO courseDTO,
                            BindingResult bindingResult, Model model) {
        model.addAttribute("lecturerList", userService.findUsersByRoleType(Role.ROLE_LECTURER));
        if (bindingResult.hasErrors()) {
            return "/createCourse";
        }
        courseDTO.setAmountStudent(0L);
        courseService.saveCourse(courseDTO);
        return "redirect:/courses/create?success";
    }
}
