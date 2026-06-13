package ui;

import models.Employee;

import javax.swing.*;
import java.awt.*;

/**
 * EmployeeDashboard - kept original design but avoid direct Main access for viewProfile.
 */
public class EmployeeDashboard extends JFrame {

    private final Employee employee;

    public EmployeeDashboard(Employee employee) {
        this.employee = employee;
        setTitle("Employee Dashboard - " + (employee != null ? employee.getName() : ""));
        setSize(480, 360);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(8,8));

        JLabel header = new JLabel("Welcome, " + (employee != null ? employee.getName() : "User"), SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 18));
        add(header, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(4,1,10,10));

        JButton viewProfileBtn = new JButton("View Profile");
        JButton viewAttendanceBtn = new JButton("My Attendance");
        JButton requestLeaveBtn = new JButton("Request Leave");
        JButton logoutBtn = new JButton("Logout");

        center.add(viewProfileBtn);
        center.add(viewAttendanceBtn);
        center.add(requestLeaveBtn);
        center.add(logoutBtn);

        add(center, BorderLayout.CENTER);

        // Actions
        viewProfileBtn.addActionListener(e -> showProfile());

        viewAttendanceBtn.addActionListener(e -> {
            try {
                if (Main.attendanceService == null) {
                    JOptionPane.showMessageDialog(this, "Attendance service not available.");
                    return;
                }
                java.util.List<?> records = Main.attendanceService.getAttendanceForEmployee(employee.getId());
                StringBuilder sb = new StringBuilder();
                for (Object o : records) sb.append(o.toString()).append("\n");
                JTextArea ta = new JTextArea(sb.length() > 0 ? sb.toString() : "No attendance records.");
                ta.setEditable(false);
                JScrollPane sp = new JScrollPane(ta);
                sp.setPreferredSize(new Dimension(450,300));
                JOptionPane.showMessageDialog(this, sp, "My Attendance", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Cannot load attendance: " + ex.getMessage());
            }
        });

        requestLeaveBtn.addActionListener(e -> {
            try {
                if (Main.leaveService == null) {
                    JOptionPane.showMessageDialog(this, "Leave service not available.");
                    return;
                }
                new SubmitLeaveGUI(employee, Main.leaveService).setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Cannot open leave form: " + ex.getMessage());
            }
        });

        logoutBtn.addActionListener(e -> {
            new LoginGUI().setVisible(true);
            this.dispose();
        });
    }

    private void showProfile() {
        if (employee == null) {
            JOptionPane.showMessageDialog(this, "Employee data not available.");
            return;
        }
        String info = String.format(
                "ID: %d%nName: %s%nDepartment: %s%nJob Title: %s%nSalary: %.2f",
                employee.getId(),
                employee.getName(),
                employee.getDepartment(),
                employee.getJobTitle(),
                employee.getSalary()
        );
        JOptionPane.showMessageDialog(this, info, "My Profile", JOptionPane.INFORMATION_MESSAGE);
    }
}

