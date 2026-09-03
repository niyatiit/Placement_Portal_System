package com.placement.portal.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "applications",
    uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "job_posting_id"}))
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_posting_id")
    private JobPosting jobPosting;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status = ApplicationStatus.APPLIED;

    private LocalDateTime appliedAt;
    private String remarks;

    @PrePersist
    protected void onCreate() { appliedAt = LocalDateTime.now(); }

    public enum ApplicationStatus {
        APPLIED, SHORTLISTED, APTITUDE_TEST, TECHNICAL_INTERVIEW,
        HR_INTERVIEW, SELECTED, REJECTED, ON_HOLD
    }

    public Application() {}

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Student getStudent() { return student; }
    public void setStudent(Student s) { this.student = s; }
    public JobPosting getJobPosting() { return jobPosting; }
    public void setJobPosting(JobPosting j) { this.jobPosting = j; }
    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus s) { this.status = s; }
    public LocalDateTime getAppliedAt() { return appliedAt; }
    public void setAppliedAt(LocalDateTime t) { this.appliedAt = t; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String r) { this.remarks = r; }
}
