package com.app.faculty.repository;

import com.app.faculty.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CourseRepository extends JpaRepository<Course, Long> {
    void deleteById(Long id);
    @Query("SELECT c FROM Course c WHERE c.topic LIKE %?1%"
            + " OR c.userLecturer.lastName LIKE %?1%" +
            "OR c.title LIKE %?1%" +
            "OR CONCAT(c.dateStart, '') LIKE %?1%" +
            "OR CONCAT(c.dateEnd, '') LIKE %?1%")
    Page<Course> findAll(String keyword, Pageable pageable);
}
