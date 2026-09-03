package com.placement.portal.controller;

import com.placement.portal.service.CompanyService;
import com.placement.portal.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final StudentService studentService;
    private final CompanyService companyService;

    public AuthController(StudentService studentService, CompanyService companyService) {
        this.studentService = studentService;
        this.companyService = companyService;
    }

    @GetMapping("/login")
    public String login() { return "auth/login"; }

    @GetMapping("/register")
    public String register(@RequestParam(defaultValue = "student") String type, Model model) {
        model.addAttribute("type", type);
        return "auth/register";
    }

    @PostMapping("/register/student")
    public String doRegisterStudent(
            @RequestParam String name, @RequestParam String email,
            @RequestParam String password, @RequestParam String rollNumber,
            @RequestParam String department, @RequestParam String batch,
            @RequestParam Double cgpa,
            @RequestParam(required = false) String phone,
            @RequestParam(defaultValue = "0") Integer backlogs,
            @RequestParam(required = false) Double tenthPercentage,
            @RequestParam(required = false) Double twelfthPercentage,
            RedirectAttributes ra) {
        try {
            studentService.register(name, email, password, rollNumber,
                    department, batch, cgpa, phone, backlogs,
                    tenthPercentage, twelfthPercentage);
            ra.addFlashAttribute("success", "Registration successful! Please login.");
            return "redirect:/auth/login";
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/auth/register?type=student";
        }
    }

    @PostMapping("/register/company")
    public String doRegisterCompany(
            @RequestParam String companyName, @RequestParam String email,
            @RequestParam String password,
            @RequestParam(required = false) String industry,
            @RequestParam(required = false) String website,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String contactPerson,
            @RequestParam(required = false) String phone,
            RedirectAttributes ra) {
        try {
            companyService.register(companyName, email, password, industry,
                    website, description, location, contactPerson, phone);
            ra.addFlashAttribute("success", "Company registered! Await admin approval before logging in.");
            return "redirect:/auth/login";
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/auth/register?type=company";
        }
    }
}
