package com.example.employee_manager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Full name is required")
    @Column(nullable = false, length = 100)
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email address")
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone must contain 10 to 15 digits (optional leading +)")
    @Column(nullable = false, length = 16)
    private String phone;

    @NotBlank(message = "Department is required")
    @Column(nullable = false, length = 80)
    private String department;

    @Positive(message = "Salary must be greater than 0")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal salary;
}
