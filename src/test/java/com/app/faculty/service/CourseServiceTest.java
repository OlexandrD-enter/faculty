package com.app.faculty.service;

import com.app.faculty.dto.CourseDTO;
import com.app.faculty.model.Course;
import com.app.faculty.model.Role;
import com.app.faculty.model.User;
import com.app.faculty.repository.CourseRepository;
import com.app.faculty.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties") // Вказує шлях до тестових налаштувань
public class CourseServiceTest {
    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindAll() {
        User user = new User(1L, "john", "doe", "email",
                "username","pass", true, Role.ROLE_LECTURER);
        userRepository.save(user);

        Course course1 = new Course(1L, "Title1", "Topic1",
                LocalDateTime.now(),
        LocalDateTime.now(),
                0L, user);
        courseRepository.save(course1);

        Course course2 = new Course(2L, "Title2", "Topic3",
                LocalDateTime.now(),
                LocalDateTime.now(),
                0L, user);
        courseRepository.save(course2);

        Course course3 = new Course(3L, "Title2", "Topic3",
                LocalDateTime.now(),
                LocalDateTime.now(),
                0L, user);
        courseRepository.save(course3);

        List<Course> courses = courseService.findAll();

        assertFalse(courses.isEmpty());
        assertEquals(3, courses.size());
        assertEquals("Title1", courses.stream().filter(course -> course.getId() == 1).findFirst().get().getTitle());
    }

    @Test
    public void testDeleteById() {
        User user = new User(1L, "john", "doe", "email",
                "username","pass", true, Role.ROLE_LECTURER);
        userRepository.save(user);

        Course course = new Course(1L, "Title1", "Topic1",
                LocalDateTime.now(),
                LocalDateTime.now(),
                0L, user);
        courseRepository.save(course);

        courseService.deleteById(course.getId());

        Optional<Course> deletedCourseOptional = courseRepository.findById(course.getId());
        assertFalse(deletedCourseOptional.isPresent());
    }

    @Test
    public void testFindCourseById_CourseFound() {
        User user = new User(1L, "john", "doe", "email",
                "username", "pass", true, Role.ROLE_LECTURER);
        userRepository.save(user);

        Course course = new Course(1L, "Title1", "Topic1",
                LocalDateTime.now(),
                LocalDateTime.now(),
                0L, user);

        Course savedCourse = courseRepository.save(course);
        Course foundCourse = courseService.findCourseById(savedCourse.getId());

        assertNotNull(foundCourse);
        assertEquals(savedCourse.getId(), foundCourse.getId());
        assertEquals(savedCourse.getTitle(), foundCourse.getTitle());
        assertEquals(savedCourse.getTopic(), foundCourse.getTopic());
    }

    @Test(expected = NoSuchElementException.class)
    public void testFindCourseById_CourseNotFound() {
        courseService.findCourseById(1234L);
    }

    @Test
    public void testSaveCourse_SuccessfulSave() {
        User user = new User(1L, "john", "doe", "email",
                "username","pass", true, Role.ROLE_LECTURER);
        userRepository.save(user);

        CourseDTO course = new CourseDTO(1L, "Title1", "Topic1",
                LocalDateTime.now(),
                LocalDateTime.now(),
                0L, user);

        Course savedCourse = courseService.saveCourse(course);

        assertNotNull(savedCourse.getId());
        assertEquals(course.getAmountStudent(), savedCourse.getAmountStudent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveCourse_FailedSave() {
        User user = new User();
        CourseDTO courseDTO = new CourseDTO();

        courseDTO.setId(1L);
        courseDTO.setUserLecturer(user);

        courseService.saveCourse(courseDTO);
    }
}