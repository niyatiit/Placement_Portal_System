package com.placement.portal.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(unique = true)
    private String rollNumber;
    private String department;
    private String batch;
    private Double cgpa;
    private String phone;
    private Integer backlogs = 0;
    private Double tenthPercentage;
    private Double twelfthPercentage;
    private String resumePath;

    @Enumerated(EnumType.STRING)
    private PlacementStatus placementStatus = PlacementStatus.NOT_PLACED;
    private String placedCompany;
    private Double placementPackage;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "student_skills", joinColumns = @JoinColumn(name = "student_id"))
    @Column(name = "skill")
    private List<String> skills = new ArrayList<>();

    public enum PlacementStatus { NOT_PLACED, PLACED, OPTED_OUT }

    public Student() {}

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String r) { this.rollNumber = r; }
    public String getDepartment() { return department; }
    public void setDepartment(String d) { this.department = d; }
    public String getBatch() { return batch; }
    public void setBatch(String b) { this.batch = b; }
    public Double getCgpa() { return cgpa; }
    public void setCgpa(Double c) { this.cgpa = c; }
    public String getPhone() { return phone; }
    public void setPhone(String p) { this.phone = p; }
    public Integer getBacklogs() { return backlogs; }
    public void setBacklogs(Integer b) { this.backlogs = b; }
    public Double getTenthPercentage() { return tenthPercentage; }
    public void setTenthPercentage(Double t) { this.tenthPercentage = t; }
    public Double getTwelfthPercentage() { return twelfthPercentage; }
    public void setTwelfthPercentage(Double t) { this.twelfthPercentage = t; }
    public String getResumePath() { return resumePath; }
    public void setResumePath(String r) { this.resumePath = r; }
    public PlacementStatus getPlacementStatus() { return placementStatus; }
    public void setPlacementStatus(PlacementStatus s) { this.placementStatus = s; }
    public String getPlacedCompany() { return placedCompany; }
    public void setPlacedCompany(String c) { this.placedCompany = c; }
    public Double getPlacementPackage() { return placementPackage; }
    public void setPlacementPackage(Double p) { this.placementPackage = p; }
    public List<String> getSkills() { return skills; }
    public void setSkills(List<String> s) { this.skills = s; }
}
