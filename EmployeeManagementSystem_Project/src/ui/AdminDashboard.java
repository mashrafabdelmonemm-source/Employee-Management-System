package ui;

import javax.swing.*;
import java.awt.*;

/**
 * AdminDashboard - kept original API but added null-checking & safer instantiation.
 */
public class AdminDashboard extends JFrame {

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(600, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10,10));

        JLabel header = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 20));
        add(header, BorderLayout.NORTH);

        // Buttons panel
        JPanel center = new JPanel(new GridLayout(3, 2, 10, 10));
        JButton manageEmployeesBtn = new JButton("Manage Employees");
        JButton manageDepartmentsBtn = new JButton("Manage Departments");
        JButton attendanceBtn = new JButton("Attendance Records");
        JButton payrollBtn = new JButton("Payroll Management");
        JButton leaveBtn = new JButton("Manage Leave Requests");
        JButton logoutBtn = new JButton("Logout");

        center.add(manageEmployeesBtn);
        center.add(manageDepartmentsBtn);
        center.add(attendanceBtn);
        center.add(payrollBtn);
        center.add(leaveBtn);
        center.add(logoutBtn);

        add(center, BorderLayout.CENTER);

        // Action listeners - use Main static services (kept original design)
        manageEmployeesBtn.addActionListener(e -> {
            try {
                if (Main.employeeService == null) {
                    JOptionPane.showMessageDialog(this, "Employee service not initialized.");
                    return;
                }
                new ManageEmployeesGUI(Main.employeeService).setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Cannot open Manage Employees: " + ex.getMessage());
            }
        });

        manageDepartmentsBtn.addActionListener(e -> {
            try {
                if (Main.departmentService == null) {
                    JOptionPane.showMessageDialog(this, "Department service not initialized.");
                    return;
                }
                new ManageDepartmentsGUI(Main.departmentService).setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Cannot open Manage Departments: " + ex.getMessage());
            }
        });

        attendanceBtn.addActionListener(e -> {
            try {
                if (Main.attendanceService == null || Main.employeeService == null) {
                    JOptionPane.showMessageDialog(this, "Attendance or Employee service not initialized.");
                    return;
                }
                new AttendanceGUI(Main.attendanceService, Main.employeeService).setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Cannot open Attendance: " + ex.getMessage());
            }
        });

        payrollBtn.addActionListener(e -> {
            try {
                if (Main.payrollService == null || Main.employeeService == null || Main.attendanceService == null) {
                    JOptionPane.showMessageDialog(this, "Payroll/Employee/Attendance service not initialized.");
                    return;
                }
                new PayrollGUI(Main.payrollService, Main.employeeService, Main.attendanceService).setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Cannot open Payroll: " + ex.getMessage());
            }
        });

        leaveBtn.addActionListener(e -> {
            try {
                if (Main.leaveService == null || Main.employeeService == null) {
                    JOptionPane.showMessageDialog(this, "Leave or Employee service not initialized.");
                    return;
                }
                new LeaveManagementGUI(Main.leaveService, Main.employeeService).setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Cannot open Leave Management: " + ex.getMessage());
            }
        });

        logoutBtn.addActionListener(e -> {
            // Return to login screen
            new LoginGUI().setVisible(true);
            this.dispose();
        });
    }
}

