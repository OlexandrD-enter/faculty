package com.app.faculty.repository;

import com.app.faculty.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    void deleteById(Long id);
    @Query("SELECT c FROM Course c WHERE c.dateEnd > :localDateTime AND " +
            "(c.topic LIKE %:keyword% OR c.userLecturer.lastName LIKE %:keyword% " +
            "OR c.title LIKE %:keyword% OR CONCAT(c.dateStart, '') LIKE %:keyword% " +
            "OR CONCAT(c.dateEnd, '') LIKE %:keyword%)")
    Page<Course> findAll(@Param("keyword") String keyword, Pageable pageable, @Param("localDateTime") LocalDateTime localDateTime);

    Page<Course> findAllByDateEndAfter(Pageable pageable, LocalDateTime localDateTime);
}
