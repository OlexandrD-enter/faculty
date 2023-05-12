package com.app.faculty.service;

import com.app.faculty.exception.MyDuplicateEntryException;
import com.app.faculty.model.Course;
import com.app.faculty.model.UserCourses;
import com.app.faculty.repository.CourseRepository;
import com.app.faculty.repository.UserCoursesRepository;
import com.app.faculty.repository.UserRepository;
import com.app.faculty.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserCourseService {

    private final UserCoursesRepository userCoursesRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public UserCourseService(UserCoursesRepository userCoursesRepository, UserRepository userRepository,
                             CourseRepository courseRepository) {
        this.userCoursesRepository = userCoursesRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    public UserCourses findById(Long id) {
        return userCoursesRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Not found userCourse with id: " + id));
    }

    @Transactional
    public UserCourses enrollOnCourse(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) auth.getPrincipal();
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Not found course with id: " + id));
        course.setAmountStudent(course.getAmountStudent() + 1);
        try {
            return userCoursesRepository.save(UserCourses.builder()
                    .course(course)
                    .user(user.getUser())
                    .build());
        } catch (DataIntegrityViolationException ex) {
            throw new MyDuplicateEntryException("Duplicate entry", ex);
        }
    }

    public List<UserCourses> presentCourses(Long id) {
        return userCoursesRepository.findPresentCourses(id, LocalDateTime.now());
    }

    public List<UserCourses> futureCourses(Long id) {
        return userCoursesRepository.findFutureCourses(id, LocalDateTime.now());
    }

    public List<UserCourses> pastCourses(Long id) {
        return userCoursesRepository.findPastCourses(id, LocalDateTime.now());
    }


}
