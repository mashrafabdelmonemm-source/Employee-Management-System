package services;

import models.AttendanceRecord;
import models.Employee;
import models.Payroll;
import storage.IStorage;

import java.util.ArrayList;
import java.util.List;

/**
 * PayrollService - fixes:
 * - null-safe load
 * - generateNewId fixed
 * - calculatePayroll separated logic with safer math
 * Note: still uses simple 160 hours baseline as original design.
 */
public class PayrollService {

    private final IStorage<Payroll> storage;
    private final List<Payroll> payrolls;

    public PayrollService(IStorage<Payroll> storage) {
        this.storage = storage;
        List<Payroll> loaded = storage.load();
        this.payrolls = (loaded != null) ? loaded : new ArrayList<>();
    }

    public Payroll calculatePayroll(Employee employee, List<AttendanceRecord> attendance) {
        double totalHours = 0;
        if (attendance != null) {
            for (AttendanceRecord r : attendance) totalHours += r.getTotalHours();
        }

        double hourlyRate = (employee.getSalary() / 160.0); // assumption
        double earned = hourlyRate * totalHours;

        double deductions = 0;
        if (totalHours < 160) deductions = (160 - totalHours) * hourlyRate;

        double finalSalary = earned - deductions;
        if (finalSalary < 0) finalSalary = 0;

        Payroll p = new Payroll(generateNewId(), employee.getId(), employee.getSalary(), totalHours, deductions, finalSalary);
        payrolls.add(p);
        storage.save(payrolls);
        return p;
    }

    public List<Payroll> getAllPayroll() {
        return new ArrayList<>(payrolls);
    }

    public boolean addPayroll(Payroll p) {
        if (p == null) return false;
        payrolls.add(p);
        storage.save(payrolls);
        return true;
    }

    public int generateNewId() {
        return payrolls.stream().mapToInt(Payroll::getId).max().orElse(0) + 1;
    }
}



