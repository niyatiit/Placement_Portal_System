package com.placement.portal.repository;

import com.placement.portal.model.Company;
import com.placement.portal.model.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {

    List<JobPosting> findByCompany(Company company);

    @Query("SELECT j FROM JobPosting j WHERE j.status = 'ACTIVE' AND j.applicationDeadline >= :today")
    List<JobPosting> findActiveJobs(@Param("today") LocalDate today);

    @Query("SELECT j FROM JobPosting j WHERE j.status = 'ACTIVE' " +
           "AND j.minCgpa <= :cgpa AND j.maxBacklogs >= :backlogs " +
           "AND j.applicationDeadline >= :today")
    List<JobPosting> findEligibleJobs(@Param("cgpa") Double cgpa,
                                      @Param("backlogs") Integer backlogs,
                                      @Param("today") LocalDate today);

    @Query("SELECT COUNT(j) FROM JobPosting j WHERE j.status = 'ACTIVE'")
    Long countActiveJobs();
}
