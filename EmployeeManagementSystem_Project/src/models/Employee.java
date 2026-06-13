

package models;

import java.util.Objects;

/**
 * Employee Model
 * - Represents both Admin and normal Employee
 * - Supports JSON storage
 * - Used across: Login, GUI, Services
 */
public class Employee {

    // ===== Core Fields =====
    private int id;
    private String name;
    private String department;
    private String jobTitle;
    private double salary;

    // ===== Login Fields =====
    private String username;
    private String password;

    // ===== Constructors =====

    /**
     * Empty constructor (Required for JSON libraries like Gson)
     */
    public Employee() {}

    /**
     * Full constructor (Admin / Custom login)
     */
    public Employee(int id,
                    String name,
                    String department,
                    String jobTitle,
                    double salary,
                    String username,
                    String password) {

        setId(id);
        setName(name);
        setDepartment(department);
        setJobTitle(jobTitle);
        setSalary(salary);
        setUsername(username);
        setPassword(password);
    }

    /**
     * Simplified constructor (Used by GUI when adding employees)
     * Default login:
     *  - username = employee id
     *  - password = 1234
     */
    public Employee(int id,
                    String name,
                    String department,
                    String jobTitle,
                    double salary) {

        this(id, name, department, jobTitle, salary,
                String.valueOf(id), "1234");
    }

    // ===== Getters & Setters =====

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id < 0)
            throw new IllegalArgumentException("ID must be non-negative");
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Name cannot be empty");
        this.name = name.trim();
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = (department == null) ? "" : department.trim();
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = (jobTitle == null) ? "" : jobTitle.trim();
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        if (salary < 0)
            throw new IllegalArgumentException("Salary cannot be negative");
        this.salary = salary;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty())
            throw new IllegalArgumentException("Username cannot be empty");
        this.username = username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null || password.trim().isEmpty())
            throw new IllegalArgumentException("Password cannot be empty");
        this.password = password.trim();
    }

    // ===== Utility Methods =====

    /**
     * Check login credentials
     */
    public boolean checkPassword(String input) {
        return password.equals(input);
    }

    /**
     * Role check
     */
    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(username);
    }

    @Override
    public String toString() {
        return String.format(
                "Employee{id=%d, name='%s', dept='%s', job='%s', salary=%.2f, username='%s'}",
                id, name, department, jobTitle, salary, username
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        Employee employee = (Employee) o;
        return id == employee.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
