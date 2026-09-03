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
public class StudentService {

    private final StudentRepository studentRepo;
    private final UserRepository userRepo;
    private final JobPostingRepository jobRepo;
    private final ApplicationRepository appRepo;
    private final PasswordEncoder encoder;

    public StudentService(StudentRepository studentRepo, UserRepository userRepo,
                          JobPostingRepository jobRepo, ApplicationRepository appRepo,
                          PasswordEncoder encoder) {
        this.studentRepo = studentRepo;
        this.userRepo = userRepo;
        this.jobRepo = jobRepo;
        this.appRepo = appRepo;
        this.encoder = encoder;
    }

    public Student register(String name, String email, String password,
                            String rollNumber, String department, String batch,
                            Double cgpa, String phone, Integer backlogs,
                            Double tenth, Double twelfth) {
        if (userRepo.existsByEmail(email))
            throw new RuntimeException("Email already registered!");

        User u = new User();
        u.setName(name);
        u.setEmail(email);
        u.setPassword(encoder.encode(password));
        u.setRole(User.Role.STUDENT);
        u.setEnabled(true);
        userRepo.saveAndFlush(u);

        Student s = new Student();
        s.setUser(u);
        s.setRollNumber(rollNumber);
        s.setDepartment(department);
        s.setBatch(batch);
        s.setCgpa(cgpa);
        s.setPhone(phone);
        s.setBacklogs(backlogs != null ? backlogs : 0);
        s.setTenthPercentage(tenth);
        s.setTwelfthPercentage(twelfth);
        s.setPlacementStatus(Student.PlacementStatus.NOT_PLACED);
        return studentRepo.saveAndFlush(s);
    }

    public Optional<Student> findByEmail(String email) {
        return studentRepo.findByUserEmail(email);
    }

    // Use JOIN FETCH to avoid LazyInitializationException
    public List<Student> findAll() {
        return studentRepo.findAllWithUser();
    }

    public List<Student> findByDept(String dept) {
        return studentRepo.findByDepartmentWithUser(dept);
    }

    public List<Student> findPlaced() {
        return studentRepo.findByPlacementStatusWithUser(Student.PlacementStatus.PLACED);
    }

    public List<JobPosting> getEligibleJobs(Student s) {
        return jobRepo.findEligibleJobs(
                s.getCgpa() != null ? s.getCgpa() : 0.0,
                s.getBacklogs() != null ? s.getBacklogs() : 0,
                LocalDate.now());
    }

    public Application apply(Student s, Long jobId) {
        JobPosting job = jobRepo.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        if (appRepo.existsByStudentAndJobPosting(s, job))
            throw new RuntimeException("You have already applied for this job!");
        Application a = new Application();
        a.setStudent(s);
        a.setJobPosting(job);
        a.setStatus(Application.ApplicationStatus.APPLIED);
        return appRepo.save(a);
    }

    public List<Application> getApplications(Student s) {
        return appRepo.findByStudentIdDesc(s.getId());
    }

    public Student save(Student s) {
        return studentRepo.save(s);
    }

    public long countPlaced() {
        return studentRepo.countPlacedStudents();
    }

    public long countAll() {
        return studentRepo.count();
    }
}
