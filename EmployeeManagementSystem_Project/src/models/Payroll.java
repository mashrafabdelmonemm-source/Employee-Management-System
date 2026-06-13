package models;

import java.util.Objects;

/**
 * Payroll model - added safety checks in setters.
 * Note: no date fields added here to avoid structural changes, but recommended.
 */
public class Payroll {

    private int id;
    private int employeeId;
    private double baseSalary;
    private double totalHours;
    private double deductions;
    private double finalSalary;

    public Payroll() {}

    public Payroll(int id, int employeeId, double baseSalary,
                   double totalHours, double deductions, double finalSalary) {
        this.setId(id);
        this.setEmployeeId(employeeId);
        this.setBaseSalary(baseSalary);
        this.setTotalHours(totalHours);
        this.setDeductions(deductions);
        this.setFinalSalary(finalSalary);
    }

    public int getId() { return id; }
    public void setId(int id) {
        if (id < 0) throw new IllegalArgumentException("id must be non-negative");
        this.id = id;
    }

    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }

    public double getBaseSalary() { return baseSalary; }
    public void setBaseSalary(double baseSalary) {
        if (Double.isNaN(baseSalary) || Double.isInfinite(baseSalary))
            throw new IllegalArgumentException("baseSalary must be valid");
        if (baseSalary < 0) throw new IllegalArgumentException("baseSalary cannot be negative");
        this.baseSalary = baseSalary;
    }

    public double getTotalHours() { return totalHours; }
    public void setTotalHours(double totalHours) {
        if (Double.isNaN(totalHours) || Double.isInfinite(totalHours)) totalHours = 0;
        this.totalHours = Math.max(0, totalHours);
    }

    public double getDeductions() { return deductions; }
    public void setDeductions(double deductions) {
        if (Double.isNaN(deductions) || Double.isInfinite(deductions)) deductions = 0;
        this.deductions = Math.max(0, deductions);
    }

    public double getFinalSalary() { return finalSalary; }
    public void setFinalSalary(double finalSalary) {
        if (Double.isNaN(finalSalary) || Double.isInfinite(finalSalary)) finalSalary = 0;
        this.finalSalary = Math.max(0, finalSalary);
    }

    @Override
    public String toString() {
        return "Payroll{" +
                "id=" + id +
                ", employeeId=" + employeeId +
                ", baseSalary=" + baseSalary +
                ", totalHours=" + totalHours +
                ", deductions=" + deductions +
                ", finalSalary=" + finalSalary +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Payroll)) return false;
        Payroll payroll = (Payroll) o;
        return id == payroll.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

