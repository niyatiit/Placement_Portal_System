package com.placement.portal.controller;

import com.placement.portal.model.Student;
import com.placement.portal.service.StudentService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    private Student getStudent(Authentication auth) {
        return studentService.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Student profile not found for: " + auth.getName()));
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {
        Student s = getStudent(auth);
        var apps = studentService.getApplications(s);
        model.addAttribute("student", s);
        model.addAttribute("eligibleJobs", studentService.getEligibleJobs(s).size());
        model.addAttribute("applicationCount", apps.size());
        model.addAttribute("recentApps", apps.stream().limit(5).toList());
        return "student/dashboard";
    }

    @GetMapping("/jobs")
    public String jobs(Model model, Authentication auth) {
        Student s = getStudent(auth);
        var appliedIds = studentService.getApplications(s)
                .stream().map(a -> a.getJobPosting().getId()).toList();
        model.addAttribute("student", s);
        model.addAttribute("jobs", studentService.getEligibleJobs(s));
        model.addAttribute("appliedIds", appliedIds);
        return "student/jobs";
    }

    @PostMapping("/apply/{jobId}")
    public String apply(@PathVariable Long jobId, Authentication auth, RedirectAttributes ra) {
        try {
            studentService.apply(getStudent(auth), jobId);
            ra.addFlashAttribute("success", "Applied successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/student/jobs";
    }

    @GetMapping("/applications")
    public String applications(Model model, Authentication auth) {
        Student s = getStudent(auth);
        model.addAttribute("student", s);
        model.addAttribute("apps", studentService.getApplications(s));
        return "student/applications";
    }

    @GetMapping("/profile")
    public String profile(Model model, Authentication auth) {
        model.addAttribute("student", getStudent(auth));
        return "student/profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam(required = false) String phone,
                                @RequestParam(required = false) Double cgpa,
                                @RequestParam(required = false) String resumePath,
                                Authentication auth, RedirectAttributes ra) {
        Student s = getStudent(auth);
        if (phone != null && !phone.isEmpty()) s.setPhone(phone);
        if (cgpa != null) s.setCgpa(cgpa);
        if (resumePath != null && !resumePath.isEmpty()) s.setResumePath(resumePath);
        studentService.save(s);
        ra.addFlashAttribute("success", "Profile updated!");
        return "redirect:/student/profile";
    }
}
