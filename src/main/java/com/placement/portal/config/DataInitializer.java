package com.placement.portal.config;

import com.placement.portal.model.*;
import com.placement.portal.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepo;
    private final StudentRepository studentRepo;
    private final CompanyRepository companyRepo;
    private final JobPostingRepository jobRepo;
    private final PasswordEncoder encoder;

    public DataInitializer(UserRepository userRepo, StudentRepository studentRepo,
                           CompanyRepository companyRepo, JobPostingRepository jobRepo,
                           PasswordEncoder encoder) {
        this.userRepo = userRepo; this.studentRepo = studentRepo;
        this.companyRepo = companyRepo; this.jobRepo = jobRepo; this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        if (userRepo.count() > 0) return;
        System.out.println(">>> Seeding demo data...");

        // Admin
        User admin = new User();
        admin.setName("Admin"); admin.setEmail("admin@placehub.com");
        admin.setPassword(encoder.encode("admin123"));
        admin.setRole(User.Role.ADMIN); admin.setEnabled(true);
        userRepo.saveAndFlush(admin);

        // Student 1
        User u1 = new User();
        u1.setName("Rahul Sharma"); u1.setEmail("rahul@student.com");
        u1.setPassword(encoder.encode("student123"));
        u1.setRole(User.Role.STUDENT); u1.setEnabled(true);
        userRepo.saveAndFlush(u1);

        Student s1 = new Student();
        s1.setUser(u1); s1.setRollNumber("CS2021001");
        s1.setDepartment("Computer Science"); s1.setBatch("2021-2025");
        s1.setCgpa(8.5); s1.setPhone("9876543210"); s1.setBacklogs(0);
        s1.setTenthPercentage(92.0); s1.setTwelfthPercentage(88.0);
        s1.setPlacementStatus(Student.PlacementStatus.NOT_PLACED);
        studentRepo.saveAndFlush(s1);

        // Student 2
        User u2 = new User();
        u2.setName("Priya Patel"); u2.setEmail("priya@student.com");
        u2.setPassword(encoder.encode("student123"));
        u2.setRole(User.Role.STUDENT); u2.setEnabled(true);
        userRepo.saveAndFlush(u2);

        Student s2 = new Student();
        s2.setUser(u2); s2.setRollNumber("CS2021002");
        s2.setDepartment("Computer Science"); s2.setBatch("2021-2025");
        s2.setCgpa(9.1); s2.setPhone("9876543211"); s2.setBacklogs(0);
        s2.setPlacementStatus(Student.PlacementStatus.NOT_PLACED);
        studentRepo.saveAndFlush(s2);

        // Student 3
        User u3 = new User();
        u3.setName("Amit Kumar"); u3.setEmail("amit@student.com");
        u3.setPassword(encoder.encode("student123"));
        u3.setRole(User.Role.STUDENT); u3.setEnabled(true);
        userRepo.saveAndFlush(u3);

        Student s3 = new Student();
        s3.setUser(u3); s3.setRollNumber("EC2021001");
        s3.setDepartment("Electronics"); s3.setBatch("2021-2025");
        s3.setCgpa(7.8); s3.setPhone("9876543212"); s3.setBacklogs(1);
        s3.setPlacementStatus(Student.PlacementStatus.NOT_PLACED);
        studentRepo.saveAndFlush(s3);

        // Company 1
        User cu1 = new User();
        cu1.setName("TechCorp HR"); cu1.setEmail("hr@techcorp.com");
        cu1.setPassword(encoder.encode("company123"));
        cu1.setRole(User.Role.COMPANY); cu1.setEnabled(true);
        userRepo.saveAndFlush(cu1);

        Company c1 = new Company();
        c1.setUser(cu1); c1.setCompanyName("TechCorp Solutions");
        c1.setIndustry("Information Technology"); c1.setWebsite("https://techcorp.com");
        c1.setDescription("Leading IT company specializing in enterprise solutions.");
        c1.setLocation("Bangalore, Karnataka"); c1.setContactPerson("TechCorp HR");
        c1.setPhone("080-12345678"); c1.setVerificationStatus(Company.VerificationStatus.VERIFIED);
        companyRepo.saveAndFlush(c1);

        // Company 2
        User cu2 = new User();
        cu2.setName("InnovateTech HR"); cu2.setEmail("hr@innovatetech.com");
        cu2.setPassword(encoder.encode("company123"));
        cu2.setRole(User.Role.COMPANY); cu2.setEnabled(true);
        userRepo.saveAndFlush(cu2);

        Company c2 = new Company();
        c2.setUser(cu2); c2.setCompanyName("InnovateTech Pvt Ltd");
        c2.setIndustry("Software Products"); c2.setWebsite("https://innovatetech.com");
        c2.setDescription("Building next-generation software products.");
        c2.setLocation("Hyderabad, Telangana"); c2.setContactPerson("InnovateTech HR");
        c2.setPhone("040-98765432"); c2.setVerificationStatus(Company.VerificationStatus.VERIFIED);
        companyRepo.saveAndFlush(c2);

        // Jobs
        JobPosting j1 = new JobPosting();
        j1.setCompany(c1); j1.setTitle("Software Engineer - Java");
        j1.setDescription("Looking for Java developers with Spring Boot experience. Work on enterprise-grade applications using microservices and cloud technologies.");
        j1.setLocation("Bangalore"); j1.setJobType(JobPosting.JobType.FULL_TIME);
        j1.setPackageLpa(8.5); j1.setMinCgpa(7.0); j1.setMaxBacklogs(0);
        j1.setEligibleDepartments("Computer Science,Information Technology");
        j1.setApplicationDeadline(LocalDate.now().plusDays(30));
        j1.setDriveDate(LocalDate.now().plusDays(45));
        j1.setStatus(JobPosting.PostingStatus.ACTIVE);
        jobRepo.saveAndFlush(j1);

        JobPosting j2 = new JobPosting();
        j2.setCompany(c1); j2.setTitle("Data Analyst Intern");
        j2.setDescription("6-month internship for data enthusiasts. Python/R proficiency preferred. Work with real business data.");
        j2.setLocation("Remote"); j2.setJobType(JobPosting.JobType.INTERNSHIP);
        j2.setPackageLpa(3.0); j2.setMinCgpa(6.5); j2.setMaxBacklogs(2);
        j2.setEligibleDepartments("Computer Science,Mathematics,Statistics");
        j2.setApplicationDeadline(LocalDate.now().plusDays(15));
        j2.setDriveDate(LocalDate.now().plusDays(25));
        j2.setStatus(JobPosting.PostingStatus.ACTIVE);
        jobRepo.saveAndFlush(j2);

        JobPosting j3 = new JobPosting();
        j3.setCompany(c2); j3.setTitle("Full Stack Developer");
        j3.setDescription("Build cutting-edge web applications. Experience with React, Node.js and cloud platforms required.");
        j3.setLocation("Hyderabad"); j3.setJobType(JobPosting.JobType.FULL_TIME);
        j3.setPackageLpa(12.0); j3.setMinCgpa(7.5); j3.setMaxBacklogs(0);
        j3.setEligibleDepartments("Computer Science,Information Technology,Electronics");
        j3.setApplicationDeadline(LocalDate.now().plusDays(20));
        j3.setDriveDate(LocalDate.now().plusDays(35));
        j3.setStatus(JobPosting.PostingStatus.ACTIVE);
        jobRepo.saveAndFlush(j3);

        System.out.println(">>> Done! Login credentials:");
        System.out.println("    Admin:    admin@placehub.com  / admin123");
        System.out.println("    Student:  rahul@student.com   / student123");
        System.out.println("    Student:  priya@student.com   / student123");
        System.out.println("    Student:  amit@student.com    / student123");
        System.out.println("    Company:  hr@techcorp.com     / company123");
        System.out.println("    Company:  hr@innovatetech.com / company123");
    }
}
