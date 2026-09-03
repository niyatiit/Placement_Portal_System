package com.placement.portal.controller;

import com.placement.portal.repository.JobPostingRepository;
import com.placement.portal.service.CompanyService;
import com.placement.portal.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final StudentService studentService;
    private final CompanyService companyService;
    private final JobPostingRepository jobRepo;

    public AdminController(StudentService studentService, CompanyService companyService,
                           JobPostingRepository jobRepo) {
        this.studentService = studentService;
        this.companyService = companyService;
        this.jobRepo = jobRepo;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalStudents", studentService.countAll());
        model.addAttribute("placedStudents", studentService.countPlaced());
        model.addAttribute("totalCompanies", companyService.findAll().size());
        model.addAttribute("activeJobs", jobRepo.countActiveJobs());
        model.addAttribute("pendingCount", companyService.findPending().size());
        model.addAttribute("recentStudents", studentService.findAll().stream().limit(5).toList());
        return "admin/dashboard";
    }

    @GetMapping("/students")
    public String students(Model model, @RequestParam(required = false) String dept) {
        var list = (dept != null && !dept.isEmpty()) ? studentService.findByDept(dept) : studentService.findAll();
        model.addAttribute("students", list);
        model.addAttribute("dept", dept);
        return "admin/students";
    }

    @GetMapping("/companies")
    public String companies(Model model) {
        model.addAttribute("companies", companyService.findAll());
        model.addAttribute("pending", companyService.findPending());
        return "admin/companies";
    }

    @PostMapping("/companies/{id}/verify")
    public String verify(@PathVariable Long id, @RequestParam boolean approved, RedirectAttributes ra) {
        companyService.verify(id, approved);
        ra.addFlashAttribute("success", approved ? "Company approved!" : "Company rejected.");
        return "redirect:/admin/companies";
    }

    @GetMapping("/jobs")
    public String jobs(Model model) {
        model.addAttribute("jobs", jobRepo.findAll());
        return "admin/jobs";
    }

    @GetMapping("/placements")
    public String placements(Model model) {
        model.addAttribute("placed", studentService.findPlaced());
        model.addAttribute("placedCount", studentService.countPlaced());
        model.addAttribute("total", studentService.countAll());
        return "admin/placements";
    }
}
