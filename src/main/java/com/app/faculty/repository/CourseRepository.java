package com.app.faculty.repository;

import com.app.faculty.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
    void deleteById(Long id);
}
