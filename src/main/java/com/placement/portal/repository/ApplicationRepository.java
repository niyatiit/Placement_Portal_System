package com.placement.portal.repository;

import com.placement.portal.model.Application;
import com.placement.portal.model.JobPosting;
import com.placement.portal.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    @Query("SELECT a FROM Application a JOIN FETCH a.jobPosting jp JOIN FETCH jp.company c JOIN FETCH c.user WHERE a.jobPosting = :jobPosting")
    List<Application> findByJobPosting(@Param("jobPosting") JobPosting jobPosting);

    Optional<Application> findByStudentAndJobPosting(Student student, JobPosting jobPosting);

    boolean existsByStudentAndJobPosting(Student student, JobPosting jobPosting);

    @Query("SELECT a FROM Application a JOIN FETCH a.jobPosting jp JOIN FETCH jp.company c JOIN FETCH c.user WHERE a.student.id = :sid ORDER BY a.appliedAt DESC")
    List<Application> findByStudentIdDesc(@Param("sid") Long sid);
}
