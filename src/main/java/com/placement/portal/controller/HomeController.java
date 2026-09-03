package com.placement.portal.controller;

import com.placement.portal.repository.JobPostingRepository;
import com.placement.portal.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.time.LocalDate;

@Controller
public class HomeController {

    private final JobPostingRepository jobRepo;
    private final StudentService studentService;

    public HomeController(JobPostingRepository jobRepo, StudentService studentService) {
        this.jobRepo = jobRepo;
        this.studentService = studentService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("activeJobs", jobRepo.findActiveJobs(LocalDate.now()));
        model.addAttribute("placedCount", studentService.countPlaced());
        model.addAttribute("totalStudents", studentService.countAll());
        return "home";
    }
}
