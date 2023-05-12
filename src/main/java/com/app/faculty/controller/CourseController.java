package com.app.faculty.controller;

import com.app.faculty.dto.CourseDTO;
import com.app.faculty.exception.MyDuplicateEntryException;
import com.app.faculty.model.Course;
import com.app.faculty.model.Role;
import com.app.faculty.model.User;
import com.app.faculty.service.CourseService;
import com.app.faculty.service.UserCourseService;
import com.app.faculty.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;
    private final UserService userService;
    private final UserCourseService userCourseService;

    @Autowired
    public CourseController(CourseService courseService, UserService userService,
                            UserCourseService userCourseService) {
        this.courseService = courseService;
        this.userService = userService;
        this.userCourseService = userCourseService;
    }

    @PostMapping()
    public String enrollAtCourse(@RequestParam("coursesId") Long courseId) {
        try {
            userCourseService.enrollOnCourse(courseId);
            return "redirect:/courses?success";
        } catch (MyDuplicateEntryException ex) {
            return "redirect:/courses?error";
        }
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
