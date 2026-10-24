package com.example.employee_manager.service;

import com.example.employee_manager.model.Employee;
import com.example.employee_manager.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmployeeService {
    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Employee> findAll(String keyword) {
        if (keyword == null || keyword.isBlank()) return repository.findAll();
        return repository.findByFullNameContainingIgnoreCase(keyword.trim());
    }

    @Transactional(readOnly = true)
    public Employee findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found: " + id));
    }

    public Employee create(Employee employee) {
        if (repository.existsByEmailIgnoreCase(employee.getEmail().trim())) {
            throw new DuplicateEmailException("This email is already registered");
        }
        employee.setEmail(employee.getEmail().trim());
        return repository.save(employee);
    }

    public Employee update(Long id, Employee updated) {
        Employee existing = findById(id);
        if (repository.existsByEmailIgnoreCaseAndIdNot(updated.getEmail().trim(), id)) {
            throw new DuplicateEmailException("This email is already registered");
        }
        existing.setFullName(updated.getFullName());
        existing.setEmail(updated.getEmail().trim());
        existing.setPhone(updated.getPhone());
        existing.setDepartment(updated.getDepartment());
        existing.setSalary(updated.getSalary());
        return repository.save(existing);
    }

    public void delete(Long id) {
        repository.delete(findById(id));
    }

    public static class DuplicateEmailException extends RuntimeException {
        public DuplicateEmailException(String message) { super(message); }
    }

    public static class EmployeeNotFoundException extends RuntimeException {
        public EmployeeNotFoundException(String message) { super(message); }
    }
}
