package com.placement.portal.repository;

import com.placement.portal.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    @Query("SELECT c FROM Company c JOIN FETCH c.user WHERE c.user.email = :email")
    Optional<Company> findByUserEmail(@Param("email") String email);

    @Query("SELECT c FROM Company c JOIN FETCH c.user")
    List<Company> findAllWithUser();

    @Query("SELECT c FROM Company c JOIN FETCH c.user WHERE c.verificationStatus = :status")
    List<Company> findByVerificationStatusWithUser(@Param("status") Company.VerificationStatus status);
}
