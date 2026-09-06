package com.placement.portal.controller;

import com.placement.portal.model.Student;
import com.placement.portal.repository.JobPostingRepository;
import com.placement.portal.service.CompanyService;
import com.placement.portal.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

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

    @GetMapping({"/students", "/student"})
    public String students(Model model,
                           @RequestParam(required = false) String dept,
                           @RequestParam(required = false) String search,
                           @RequestParam(required = false) String status) {
        List<Student> list;
        if ((dept != null && !dept.trim().isEmpty()) || (search != null && !search.trim().isEmpty()) || (status != null && !status.trim().isEmpty())) {
            list = studentService.searchStudents(
                    (dept != null && !dept.trim().isEmpty()) ? dept.trim() : null,
                    (status != null && !status.trim().isEmpty()) ? status.trim() : null,
                    (search != null && !search.trim().isEmpty()) ? search.trim() : null
            );
        } else {
            list = studentService.findAll();
        }
        model.addAttribute("students", list);
        model.addAttribute("dept", dept != null ? dept : "");
        model.addAttribute("search", search != null ? search : "");
        model.addAttribute("status", status != null ? status : "");
        model.addAttribute("statuses", Student.PlacementStatus.values());
        return "admin/students";
    }

    @PostMapping("/students/{id}/update")
    public String updateStudent(@PathVariable Long id,
                               @RequestParam String name,
                               @RequestParam(required = false) String phone,
                               @RequestParam(required = false) String department,
                               @RequestParam(required = false) String batch,
                               @RequestParam(required = false) String rollNumber,
                               @RequestParam(required = false) Double cgpa,
                               @RequestParam(required = false) Integer backlogs,
                               @RequestParam(required = false) Double tenthPercentage,
                               @RequestParam(required = false) Double twelfthPercentage,
                               @RequestParam(required = false) Student.PlacementStatus placementStatus,
                               @RequestParam(required = false) String placedCompany,
                               @RequestParam(required = false) Double placementPackage,
                               RedirectAttributes ra) {
        try {
            studentService.updateStudent(id, name, phone, department, batch, rollNumber,
                    cgpa, backlogs, tenthPercentage, twelfthPercentage, placementStatus, placedCompany, placementPackage);
            ra.addFlashAttribute("success", "Student details updated successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Failed to update student: " + e.getMessage());
        }
        return "redirect:/admin/students";
    }

    @PostMapping("/students/{id}/delete")
    public String deleteStudent(@PathVariable Long id, RedirectAttributes ra) {
        try {
            studentService.deleteStudent(id);
            ra.addFlashAttribute("success", "Student deleted successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Failed to delete student: " + e.getMessage());
        }
        return "redirect:/admin/students";
    }

    @PostMapping("/students/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam Student.PlacementStatus placementStatus,
                               @RequestParam(required = false) String placedCompany,
                               @RequestParam(required = false) Double placementPackage,
                               RedirectAttributes ra) {
        try {
            studentService.updatePlacementStatus(id, placementStatus, placedCompany, placementPackage);
            ra.addFlashAttribute("success", "Placement status updated successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Failed to update status: " + e.getMessage());
        }
        return "redirect:/admin/students";
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
