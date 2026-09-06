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

    public Optional<Student> findById(Long id) {
        return studentRepo.findByIdWithUser(id);
    }

    public List<Student> searchStudents(String dept, String status, String search) {
        List<Student> list = studentRepo.findAllWithUser();
        if ((dept == null || dept.isBlank()) && (status == null || status.isBlank()) && (search == null || search.isBlank())) {
            return list;
        }
        return list.stream().filter(s -> {
            boolean matchDept = true;
            if (dept != null && !dept.isBlank()) {
                matchDept = s.getDepartment() != null && s.getDepartment().toLowerCase().contains(dept.trim().toLowerCase());
            }

            boolean matchStatus = true;
            if (status != null && !status.isBlank()) {
                matchStatus = s.getPlacementStatus() != null && s.getPlacementStatus().name().equalsIgnoreCase(status.trim());
            }

            boolean matchSearch = true;
            if (search != null && !search.isBlank()) {
                String term = search.trim().toLowerCase();
                boolean nameMatch = s.getUser() != null && s.getUser().getName() != null && s.getUser().getName().toLowerCase().contains(term);
                boolean emailMatch = s.getUser() != null && s.getUser().getEmail() != null && s.getUser().getEmail().toLowerCase().contains(term);
                boolean rollMatch = s.getRollNumber() != null && s.getRollNumber().toLowerCase().contains(term);
                boolean phoneMatch = s.getPhone() != null && s.getPhone().contains(term);
                boolean deptMatch = s.getDepartment() != null && s.getDepartment().toLowerCase().contains(term);
                matchSearch = nameMatch || emailMatch || rollMatch || phoneMatch || deptMatch;
            }

            return matchDept && matchStatus && matchSearch;
        }).toList();
    }

    public Student updateStudent(Long id, String name, String phone, String department,
                                 String batch, String rollNumber, Double cgpa, Integer backlogs,
                                 Double tenth, Double twelfth, Student.PlacementStatus status,
                                 String placedCompany, Double placementPackage) {
        Student s = studentRepo.findByIdWithUser(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

        if (s.getUser() != null && name != null && !name.trim().isEmpty()) {
            s.getUser().setName(name.trim());
            userRepo.save(s.getUser());
        }

        s.setPhone(phone);
        s.setDepartment(department);
        s.setBatch(batch);
        s.setRollNumber(rollNumber);
        s.setCgpa(cgpa);
        s.setBacklogs(backlogs != null ? backlogs : 0);
        s.setTenthPercentage(tenth);
        s.setTwelfthPercentage(twelfth);

        if (status != null) {
            s.setPlacementStatus(status);
            if (status == Student.PlacementStatus.PLACED) {
                s.setPlacedCompany(placedCompany);
                s.setPlacementPackage(placementPackage);
            } else {
                s.setPlacedCompany(null);
                s.setPlacementPackage(null);
            }
        }

        return studentRepo.save(s);
    }

    public Student updatePlacementStatus(Long id, Student.PlacementStatus status,
                                         String placedCompany, Double placementPackage) {
        Student s = studentRepo.findByIdWithUser(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        s.setPlacementStatus(status);
        if (status == Student.PlacementStatus.PLACED) {
            s.setPlacedCompany(placedCompany);
            s.setPlacementPackage(placementPackage);
        } else {
            s.setPlacedCompany(null);
            s.setPlacementPackage(null);
        }
        return studentRepo.save(s);
    }

    public void deleteStudent(Long id) {
        Student s = studentRepo.findByIdWithUser(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        appRepo.deleteByStudent(s);
        User u = s.getUser();
        studentRepo.delete(s);
        if (u != null) {
            userRepo.delete(u);
        }
    }

    public long countPlaced() {
        return studentRepo.countPlacedStudents();
    }

    public long countAll() {
        return studentRepo.count();
    }
}
