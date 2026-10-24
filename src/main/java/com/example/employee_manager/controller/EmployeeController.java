package com.example.employee_manager.controller;

import com.example.employee_manager.model.Employee;
import com.example.employee_manager.service.EmployeeService;
import com.example.employee_manager.service.EmployeeService.DuplicateEmailException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class EmployeeController {
    private final EmployeeService service;

    public EmployeeController(EmployeeService service) { this.service = service; }

    @GetMapping("/")
    public String home() { return "redirect:/employees"; }

    @GetMapping("/employees")
    public String list(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("employees", service.findAll(keyword));
        model.addAttribute("employee", new Employee());
        model.addAttribute("keyword", keyword == null ? "" : keyword);
        return "employees";
    }

    @PostMapping("/employees")
    public String create(@Valid @ModelAttribute("employee") Employee employee,
                         BindingResult result, Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("employees", service.findAll(null));
            model.addAttribute("keyword", "");
            return "employees";
        }
        try {
            service.create(employee);
        } catch (DuplicateEmailException ex) {
            result.rejectValue("email", "duplicate", ex.getMessage());
            model.addAttribute("employees", service.findAll(null));
            model.addAttribute("keyword", "");
            return "employees";
        }
        redirect.addFlashAttribute("successMessage", "Employee saved successfully.");
        return "redirect:/employees";
    }

    @GetMapping("/employees/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("employee", service.findById(id));
        return "edit-employee";
    }

    @PostMapping("/employees/{id}/update")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("employee") Employee employee,
                         BindingResult result, Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) return "edit-employee";
        try {
            service.update(id, employee);
        } catch (DuplicateEmailException ex) {
            result.rejectValue("email", "duplicate", ex.getMessage());
            return "edit-employee";
        }
        redirect.addFlashAttribute("successMessage", "Employee updated successfully.");
        return "redirect:/employees";
    }

    @PostMapping("/employees/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        service.delete(id);
        redirect.addFlashAttribute("successMessage", "Employee deleted successfully.");
        return "redirect:/employees";
    }
}
