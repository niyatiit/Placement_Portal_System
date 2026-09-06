package com.placement.portal.repository;

import com.placement.portal.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("SELECT s FROM Student s JOIN FETCH s.user WHERE s.user.email = :email")
    Optional<Student> findByUserEmail(@Param("email") String email);

    @Query("SELECT s FROM Student s JOIN FETCH s.user")
    List<Student> findAllWithUser();

    @Query("SELECT s FROM Student s JOIN FETCH s.user WHERE s.department = :dept")
    List<Student> findByDepartmentWithUser(@Param("dept") String dept);

    @Query("SELECT s FROM Student s JOIN FETCH s.user WHERE s.placementStatus = :status")
    List<Student> findByPlacementStatusWithUser(@Param("status") Student.PlacementStatus status);

    @Query("SELECT COUNT(s) FROM Student s WHERE s.placementStatus = 'PLACED'")
    Long countPlacedStudents();

    @Query("SELECT s FROM Student s JOIN FETCH s.user WHERE s.id = :id")
    Optional<Student> findByIdWithUser(@Param("id") Long id);

    @Query("SELECT s FROM Student s JOIN FETCH s.user WHERE " +
           "(:dept IS NULL OR :dept = '' OR LOWER(s.department) LIKE LOWER(CONCAT('%', :dept, '%'))) AND " +
           "(:status IS NULL OR s.placementStatus = :status) AND " +
           "(:search IS NULL OR :search = '' OR LOWER(s.user.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
           " OR LOWER(s.rollNumber) LIKE LOWER(CONCAT('%', :search, '%')) " +
           " OR LOWER(s.user.email) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Student> searchStudents(@Param("dept") String dept,
                                 @Param("status") Student.PlacementStatus status,
                                 @Param("search") String search);
}
