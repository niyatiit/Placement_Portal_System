package com.placement.portal.service;

import com.placement.portal.model.*;
import com.placement.portal.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CompanyService {

    private final CompanyRepository companyRepo;
    private final UserRepository userRepo;
    private final JobPostingRepository jobRepo;
    private final ApplicationRepository appRepo;
    private final StudentRepository studentRepo;
    private final PasswordEncoder encoder;

    public CompanyService(CompanyRepository companyRepo, UserRepository userRepo,
                          JobPostingRepository jobRepo, ApplicationRepository appRepo,
                          StudentRepository studentRepo, PasswordEncoder encoder) {
        this.companyRepo = companyRepo;
        this.userRepo = userRepo;
        this.jobRepo = jobRepo;
        this.appRepo = appRepo;
        this.studentRepo = studentRepo;
        this.encoder = encoder;
    }

    public Company register(String companyName, String email, String password,
                            String industry, String website, String description,
                            String location, String contactPerson, String phone) {
        if (userRepo.existsByEmail(email))
            throw new RuntimeException("Email already registered!");

        User u = new User();
        u.setName(companyName);
        u.setEmail(email);
        u.setPassword(encoder.encode(password));
        u.setRole(User.Role.COMPANY);
        u.setEnabled(true);
        userRepo.saveAndFlush(u);

        Company c = new Company();
        c.setUser(u);
        c.setCompanyName(companyName);
        c.setIndustry(industry);
        c.setWebsite(website);
        c.setDescription(description);
        c.setLocation(location);
        c.setContactPerson(contactPerson);
        c.setPhone(phone);
        c.setVerificationStatus(Company.VerificationStatus.PENDING);
        return companyRepo.saveAndFlush(c);
    }

    public Optional<Company> findByEmail(String email) {
        return companyRepo.findByUserEmail(email);
    }

    // Use JOIN FETCH to avoid LazyInitializationException
    public List<Company> findAll() {
        return companyRepo.findAllWithUser();
    }

    public List<Company> findPending() {
        return companyRepo.findByVerificationStatusWithUser(Company.VerificationStatus.PENDING);
    }

    public void verify(Long id, boolean approved) {
        companyRepo.findById(id).ifPresent(c -> {
            c.setVerificationStatus(
                approved ? Company.VerificationStatus.VERIFIED : Company.VerificationStatus.REJECTED
            );
            companyRepo.save(c);
        });
    }

    public JobPosting postJob(Company c, String title, String desc, String location,
                              JobPosting.JobType type, Double pkg, Double minCgpa,
                              Integer maxBacklogs, String depts,
                              LocalDate deadline, LocalDate drive) {
        JobPosting j = new JobPosting();
        j.setCompany(c);
        j.setTitle(title);
        j.setDescription(desc);
        j.setLocation(location);
        j.setJobType(type);
        j.setPackageLpa(pkg);
        j.setMinCgpa(minCgpa);
        j.setMaxBacklogs(maxBacklogs);
        j.setEligibleDepartments(depts);
        j.setApplicationDeadline(deadline);
        j.setDriveDate(drive);
        j.setStatus(JobPosting.PostingStatus.ACTIVE);
        return jobRepo.save(j);
    }

    public List<JobPosting> getJobs(Company c) {
        return jobRepo.findByCompany(c);
    }

    public List<Application> getAppsForJob(Long jobId) {
        return jobRepo.findById(jobId)
                .map(appRepo::findByJobPosting)
                .orElse(List.of());
    }

    public void updateStatus(Long appId, Application.ApplicationStatus status, String remarks) {
        appRepo.findById(appId).ifPresent(a -> {
            a.setStatus(status);
            a.setRemarks(remarks);
            if (status == Application.ApplicationStatus.SELECTED) {
                Student s = a.getStudent();
                s.setPlacementStatus(Student.PlacementStatus.PLACED);
                s.setPlacedCompany(a.getJobPosting().getCompany().getCompanyName());
                s.setPlacementPackage(a.getJobPosting().getPackageLpa());
                studentRepo.save(s);
            }
            appRepo.save(a);
        });
    }
}
