package com.placement.portal.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "job_postings")
public class JobPosting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    private String title;
    @Column(length = 3000)
    private String description;
    private String location;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    private Double packageLpa;
    private Double minCgpa;
    private Integer maxBacklogs;
    private String eligibleDepartments;
    private LocalDate applicationDeadline;
    private LocalDate driveDate;

    @Enumerated(EnumType.STRING)
    private PostingStatus status = PostingStatus.ACTIVE;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "jobPosting", cascade = CascadeType.ALL)
    private List<Application> applications = new ArrayList<>();

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }

    public enum JobType { FULL_TIME, INTERNSHIP, PART_TIME, CONTRACT }
    public enum PostingStatus { ACTIVE, CLOSED, DRAFT }

    public JobPosting() {}

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Company getCompany() { return company; }
    public void setCompany(Company c) { this.company = c; }
    public String getTitle() { return title; }
    public void setTitle(String t) { this.title = t; }
    public String getDescription() { return description; }
    public void setDescription(String d) { this.description = d; }
    public String getLocation() { return location; }
    public void setLocation(String l) { this.location = l; }
    public JobType getJobType() { return jobType; }
    public void setJobType(JobType j) { this.jobType = j; }
    public Double getPackageLpa() { return packageLpa; }
    public void setPackageLpa(Double p) { this.packageLpa = p; }
    public Double getMinCgpa() { return minCgpa; }
    public void setMinCgpa(Double m) { this.minCgpa = m; }
    public Integer getMaxBacklogs() { return maxBacklogs; }
    public void setMaxBacklogs(Integer m) { this.maxBacklogs = m; }
    public String getEligibleDepartments() { return eligibleDepartments; }
    public void setEligibleDepartments(String e) { this.eligibleDepartments = e; }
    public LocalDate getApplicationDeadline() { return applicationDeadline; }
    public void setApplicationDeadline(LocalDate d) { this.applicationDeadline = d; }
    public LocalDate getDriveDate() { return driveDate; }
    public void setDriveDate(LocalDate d) { this.driveDate = d; }
    public PostingStatus getStatus() { return status; }
    public void setStatus(PostingStatus s) { this.status = s; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public List<Application> getApplications() { return applications; }
    public void setApplications(List<Application> a) { this.applications = a; }
}
