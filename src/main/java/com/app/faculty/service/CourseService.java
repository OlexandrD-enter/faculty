package com.app.faculty.service;

import com.app.faculty.dto.CourseDTO;
import com.app.faculty.model.Course;
import com.app.faculty.model.User;
import com.app.faculty.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    public void deleteById(Long id) {
        courseRepository.deleteById(id);
    }

    public Course findCourseById(Long id) {
        return courseRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Not found course with id: " + id));
    }

    public Course saveCourse(CourseDTO courseDTO) {
        try {
            return courseRepository.save(Course.builder()
                    .id(courseDTO.getId())
                    .amountStudent(courseDTO.getAmountStudent())
                    .dateEnd(courseDTO.getDateEnd())
                    .dateStart(courseDTO.getDateStart())
                    .title(courseDTO.getTitle())
                    .topic(courseDTO.getTopic())
                    .userLecturer(courseDTO.getUserLecturer())
                    .build());
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed operation to save course");
        }
    }

    public Page<Course> findPaginatedCourses(int pageNum, String sortField, String sortDir, String keyword) {
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNum-1, pageSize,
                sortDir.equals("asc") ? Sort.by(sortField).ascending()
                        : Sort.by(sortField).descending()
        );
        if(keyword != null){
            return courseRepository.findAll(keyword, pageable, LocalDateTime.now());
        }
        return courseRepository.findAllByDateEndAfter(pageable, LocalDateTime.now());
    }
}
