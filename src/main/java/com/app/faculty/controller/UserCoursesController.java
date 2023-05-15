package com.app.faculty.controller;

import com.app.faculty.model.UserCourses;
import com.app.faculty.security.UserDetailsImpl;
import com.app.faculty.service.UserCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cabinet")
public class UserCoursesController {

    private final UserCourseService userCourseService;

    @Autowired
    public UserCoursesController(UserCourseService userCourseService) {
        this.userCourseService = userCourseService;
    }

    @GetMapping()
    public String showMyCabinetPage(@AuthenticationPrincipal UserDetailsImpl currentUser, Model model){
        model.addAttribute("user", currentUser.getUser());
        model.addAttribute("presentCourse",
                userCourseService.presentCourses(currentUser.getUser().getId()));
        model.addAttribute("pastCourse",
                userCourseService.pastCourses(currentUser.getUser().getId()));
        model.addAttribute("futureCourse",
                userCourseService.futureCourses(currentUser.getUser().getId()));
        model.addAttribute("lecturerCoursesPast", userCourseService.findLecturerCoursesPast(currentUser.getUser().getId()));
        model.addAttribute("lecturerCoursesPresent", userCourseService.findLecturerCoursesPresent(currentUser.getUser().getId()));
        model.addAttribute("lecturerCoursesFuture", userCourseService.findLecturerCoursesFuture(currentUser.getUser().getId()));
        return "cabinet";
    }

    @PutMapping("{id}")
    public String updateCourse(@PathVariable("id") Long courseId, @RequestParam Integer mark) {
        UserCourses userCourses = userCourseService.findById(courseId);
        userCourses.setMark(mark);
        userCourseService.save(userCourses);
        return "redirect:/cabinet";
    }
}
