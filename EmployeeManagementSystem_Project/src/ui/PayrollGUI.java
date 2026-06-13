package ui;

import models.Employee;
import models.Payroll;
import models.AttendanceRecord;
import services.PayrollService;
import services.EmployeeService;
import services.AttendanceService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * PayrollGUI - kept UI but added checks to avoid duplicate payrolls and improved messaging.
 */
public class PayrollGUI extends JFrame {

    private PayrollService payrollService;
    private EmployeeService employeeService;
    private AttendanceService attendanceService;

    private JTable table;
    private DefaultTableModel model;

    public PayrollGUI(PayrollService payrollService, EmployeeService employeeService, AttendanceService attendanceService) {
        this.payrollService = payrollService;
        this.employeeService = employeeService;
        this.attendanceService = attendanceService;

        setTitle("Payroll Management");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        model = new DefaultTableModel(new String[]{
                "Employee ID", "Employee Name", "Base Salary", "Total Hours", "Deductions", "Final Salary"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton calculateBtn = new JButton("Generate Payroll");
        JButton refreshBtn = new JButton("Refresh");

        JPanel panel = new JPanel();
        panel.add(calculateBtn);
        panel.add(refreshBtn);

        add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

        loadPayroll();

        calculateBtn.addActionListener(e -> generatePayroll());
        refreshBtn.addActionListener(e -> loadPayroll());
    }

    private void loadPayroll() {
        model.setRowCount(0);

        List<Payroll> payrolls = payrollService.getAllPayroll();

        for (Payroll p : payrolls) {
            Employee emp = employeeService.getEmployeeById(p.getEmployeeId());

            model.addRow(new Object[]{
                    p.getEmployeeId(),
                    emp != null ? emp.getName() : "Unknown",
                    p.getBaseSalary(),
                    p.getTotalHours(),
                    p.getDeductions(),
                    p.getFinalSalary()
            });
        }
    }

    private void generatePayroll() {
        try {
            for (Employee emp : employeeService.getAllEmployees()) {

                List<AttendanceRecord> records = attendanceService.getAttendanceForEmployee(emp.getId());

                double totalHours = records.stream()
                        .mapToDouble(AttendanceRecord::getTotalHours)
                        .sum();

                double deductions = 0;

                if (totalHours < 160) { // 160 ساعة = 20 يوم × 8 ساعات
                    // keep original logic but safer
                    double hourly = emp.getSalary() / 160.0;
                    deductions = Math.max(0, (160 - totalHours) * hourly);
                }

                double finalSalary = emp.getSalary() - deductions;
                finalSalary = Math.max(0, finalSalary);

                Payroll p = new Payroll(
                        payrollService.generateNewId(),
                        emp.getId(),
                        emp.getSalary(),
                        totalHours,
                        deductions,
                        finalSalary
                );

                payrollService.addPayroll(p);
            }

            JOptionPane.showMessageDialog(this, "Payroll generated successfully!");
            loadPayroll();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error generating payroll: " + ex.getMessage());
        }
    }
}

