package com.placement.portal.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String companyName;
    private String industry;
    private String website;
    @Column(length = 1000)
    private String description;
    private String location;
    private String contactPerson;
    private String phone;

    @Enumerated(EnumType.STRING)
    private VerificationStatus verificationStatus = VerificationStatus.PENDING;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<JobPosting> jobPostings = new ArrayList<>();

    public enum VerificationStatus { PENDING, VERIFIED, REJECTED }

    public Company() {}

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User u) { this.user = u; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String n) { this.companyName = n; }
    public String getIndustry() { return industry; }
    public void setIndustry(String i) { this.industry = i; }
    public String getWebsite() { return website; }
    public void setWebsite(String w) { this.website = w; }
    public String getDescription() { return description; }
    public void setDescription(String d) { this.description = d; }
    public String getLocation() { return location; }
    public void setLocation(String l) { this.location = l; }
    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String c) { this.contactPerson = c; }
    public String getPhone() { return phone; }
    public void setPhone(String p) { this.phone = p; }
    public VerificationStatus getVerificationStatus() { return verificationStatus; }
    public void setVerificationStatus(VerificationStatus v) { this.verificationStatus = v; }
    public List<JobPosting> getJobPostings() { return jobPostings; }
    public void setJobPostings(List<JobPosting> j) { this.jobPostings = j; }
}
