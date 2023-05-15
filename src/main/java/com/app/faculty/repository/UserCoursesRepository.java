package com.app.faculty.repository;


import com.app.faculty.model.UserCourses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserCoursesRepository extends JpaRepository<UserCourses, Long> {
    Optional<UserCourses> findById(Long id);

    @Query("select u from UserCourses u join Course c ON u.user.id = :id and c.dateStart < :localDateTime and c.dateEnd > :localDateTime and c.id = u.course.id group by u.id")
    List<UserCourses> findPresentCourses(long id, LocalDateTime localDateTime);

    @Query("select u from UserCourses u join Course c ON u.user.id = :id and c.dateStart > :localDateTime and c.id = u.course.id group by u.id")
    List<UserCourses> findFutureCourses(long id, LocalDateTime localDateTime);

    @Query("select u from UserCourses u join Course c ON u.user.id = :id and c.dateEnd < :localDateTime and c.id = u.course.id group by u.id")
    List<UserCourses> findPastCourses(long id, LocalDateTime localDateTime);

    List<UserCourses> findUserCoursesByCourseUserLecturerId(Long id);

    @Query("select u from UserCourses u join Course c ON u.course.userLecturer.id = :id and c.dateStart < :localDateTime and c.dateEnd > :localDateTime and c.id = u.course.id group by u.id")
    List<UserCourses> findUserCoursesByLecturerIdAndPresentCourses(Long id, LocalDateTime localDateTime);

    @Query("select u from UserCourses u join Course c ON u.course.userLecturer.id = :id and c.dateEnd < :localDateTime and c.id = u.course.id group by u.id")
    List<UserCourses> findUserCoursesByLecturerIdAndPastCourses(Long id, LocalDateTime localDateTime);

    @Query("select u from UserCourses u join Course c ON u.course.userLecturer.id = :id and c.dateStart > :localDateTime and c.id = u.course.id group by u.id")
    List<UserCourses> findUserCoursesByLecturerIdAndFutureCourses(Long id, LocalDateTime localDateTime);
}
