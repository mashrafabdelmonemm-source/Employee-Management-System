package ui;

import models.AttendanceRecord;
import models.Employee;
import services.AttendanceService;
import services.EmployeeService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * AttendanceGUI - added validations, read-only table and null-safety.
 */
public class AttendanceGUI extends JFrame {

    private AttendanceService attendanceService;
    private EmployeeService employeeService;
    private JTable table;
    private DefaultTableModel model;

    public AttendanceGUI(AttendanceService attendanceService, EmployeeService employeeService) {
        this.attendanceService = attendanceService;
        this.employeeService = employeeService;

        setTitle("Attendance Management");
        setSize(800, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Table model
        model = new DefaultTableModel(new String[]{
                "Employee ID", "Employee Name", "Date", "Check In", "Check Out", "Total Hours"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // make table read-only
            }
        };

        table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        JScrollPane scrollPane = new JScrollPane(table);

        // Buttons
        JButton checkInBtn = new JButton("Check In");
        JButton checkOutBtn = new JButton("Check Out");
        JButton refreshBtn = new JButton("Refresh");

        JPanel btnPanel = new JPanel();
        btnPanel.add(checkInBtn);
        btnPanel.add(checkOutBtn);
        btnPanel.add(refreshBtn);

        add(scrollPane, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        loadAttendance();

        // Buttons actions
        checkInBtn.addActionListener(e -> openCheckInDialog());
        checkOutBtn.addActionListener(e -> openCheckOutDialog());
        refreshBtn.addActionListener(e -> loadAttendance());
    }

    private void loadAttendance() {
        model.setRowCount(0);

        List<AttendanceRecord> list = attendanceService.getAllAttendance();
        for (AttendanceRecord record : list) {
            Employee emp = (employeeService != null) ? employeeService.getEmployeeById(record.getEmployeeId()) : null;

            model.addRow(new Object[]{
                    record.getEmployeeId(),
                    emp != null ? emp.getName() : "Unknown",
                    record.getDate(),
                    record.getCheckInTime(),
                    record.getCheckOutTime(),
                    record.getTotalHours()
            });
        }
    }

    // Add Check-in
    private void openCheckInDialog() {
        String empIdStr = JOptionPane.showInputDialog(this, "Enter Employee ID:");

        if (empIdStr == null) return;

        try {
            int empId = Integer.parseInt(empIdStr);

            // local validation
            if (employeeService != null && employeeService.getEmployeeById(empId) == null) {
                JOptionPane.showMessageDialog(this, "Employee not found!");
                return;
            }

            boolean result = attendanceService.checkIn(empId);

            if (result) {
                JOptionPane.showMessageDialog(this, "Check-in recorded!");
                loadAttendance();
            } else {
                JOptionPane.showMessageDialog(this, "Check-in failed. Employee not found or already checked in.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Employee ID!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error during check-in: " + ex.getMessage());
        }
    }

    // Add Check-out
    private void openCheckOutDialog() {
        String empIdStr = JOptionPane.showInputDialog(this, "Enter Employee ID:");

        if (empIdStr == null) return;

        try {
            int empId = Integer.parseInt(empIdStr);

            boolean result = attendanceService.checkOut(empId);

            if (result) {
                JOptionPane.showMessageDialog(this, "Check-out recorded!");
                loadAttendance();
            } else {
                JOptionPane.showMessageDialog(this, "Check-out failed. No check-in found.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Employee ID!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error during check-out: " + ex.getMessage());
        }
    }
}

