package com.app.faculty.repository;


import com.app.faculty.model.UserCourses;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCoursesRepository extends JpaRepository<UserCourses, Long> {
    Optional<UserCourses> findById(Long id);
}
