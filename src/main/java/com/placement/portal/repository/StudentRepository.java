package com.placement.portal.repository;

import com.placement.portal.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.user WHERE s.user.email = :email")
    Optional<Student> findByUserEmail(@Param("email") String email);

    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.user ORDER BY s.id ASC")
    List<Student> findAllWithUser();

    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.user WHERE s.department = :dept ORDER BY s.id ASC")
    List<Student> findByDepartmentWithUser(@Param("dept") String dept);

    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.user WHERE s.placementStatus = :status ORDER BY s.id ASC")
    List<Student> findByPlacementStatusWithUser(@Param("status") Student.PlacementStatus status);

    @Query("SELECT COUNT(s) FROM Student s WHERE s.placementStatus = 'PLACED'")
    Long countPlacedStudents();

    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.user WHERE s.id = :id")
    Optional<Student> findByIdWithUser(@Param("id") Long id);

    boolean existsByRollNumber(String rollNumber);
}