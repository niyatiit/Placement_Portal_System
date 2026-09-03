package com.placement.portal.controller;

import com.placement.portal.model.*;
import com.placement.portal.service.CompanyService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;

@Controller
@RequestMapping("/company")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    private Company getCompany(Authentication auth) {
        return companyService.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Company not found for: " + auth.getName()));
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {
        Company c = getCompany(auth);
        var jobs = companyService.getJobs(c);
        long totalApps = jobs.stream().mapToLong(j -> companyService.getAppsForJob(j.getId()).size()).sum();
        model.addAttribute("company", c);
        model.addAttribute("jobCount", jobs.size());
        model.addAttribute("totalApps", totalApps);
        model.addAttribute("recentJobs", jobs.stream().limit(3).toList());
        return "company/dashboard";
    }

    @GetMapping("/jobs")
    public String jobs(Model model, Authentication auth) {
        Company c = getCompany(auth);
        model.addAttribute("company", c);
        model.addAttribute("jobs", companyService.getJobs(c));
        return "company/jobs";
    }

    @GetMapping("/jobs/new")
    public String newJobPage(Model model, Authentication auth) {
        model.addAttribute("company", getCompany(auth));
        return "company/new-job";
    }

    @PostMapping("/jobs/new")
    public String postJob(@RequestParam String title,
                          @RequestParam String description,
                          @RequestParam(required = false) String location,
                          @RequestParam JobPosting.JobType jobType,
                          @RequestParam Double packageLpa,
                          @RequestParam Double minCgpa,
                          @RequestParam(defaultValue = "0") Integer maxBacklogs,
                          @RequestParam(required = false) String eligibleDepartments,
                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate applicationDeadline,
                          @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate driveDate,
                          Authentication auth, RedirectAttributes ra) {
        try {
            companyService.postJob(getCompany(auth), title, description, location,
                    jobType, packageLpa, minCgpa, maxBacklogs,
                    eligibleDepartments, applicationDeadline, driveDate);
            ra.addFlashAttribute("success", "Job posted successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/company/jobs";
    }

    @GetMapping("/jobs/{jobId}/applications")
    public String viewApps(@PathVariable Long jobId, Model model, Authentication auth) {
        model.addAttribute("company", getCompany(auth));
        model.addAttribute("apps", companyService.getAppsForJob(jobId));
        model.addAttribute("jobId", jobId);
        return "company/applications";
    }

    @PostMapping("/applications/{appId}/status")
    public String updateStatus(@PathVariable Long appId,
                               @RequestParam Application.ApplicationStatus status,
                               @RequestParam(required = false) String remarks,
                               @RequestParam Long jobId, RedirectAttributes ra) {
        companyService.updateStatus(appId, status, remarks);
        ra.addFlashAttribute("success", "Status updated!");
        return "redirect:/company/jobs/" + jobId + "/applications";
    }

    @GetMapping("/profile")
    public String profile(Model model, Authentication auth) {
        model.addAttribute("company", getCompany(auth));
        return "company/profile";
    }
}
