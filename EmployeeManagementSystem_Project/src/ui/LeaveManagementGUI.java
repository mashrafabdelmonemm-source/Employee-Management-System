package ui;

import models.LeaveRequest;
import models.Employee;
import services.LeaveService;
import services.EmployeeService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * LeaveManagementGUI - added null checks and protections on status update.
 */
public class LeaveManagementGUI extends JFrame {

    private LeaveService leaveService;
    private EmployeeService employeeService;
    private JTable table;
    private DefaultTableModel model;

    public LeaveManagementGUI(LeaveService leaveService, EmployeeService employeeService) {
        this.leaveService = leaveService;
        this.employeeService = employeeService;

        setTitle("Leave Management");
        setSize(800, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Table Model
        model = new DefaultTableModel(new String[]{
                "Request ID", "Employee ID", "Employee Name",
                "Start Date", "End Date", "Status"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Buttons
        JButton approveBtn = new JButton("Approve");
        JButton rejectBtn = new JButton("Reject");
        JButton refreshBtn = new JButton("Refresh");

        JPanel btnPanel = new JPanel();
        btnPanel.add(approveBtn);
        btnPanel.add(rejectBtn);
        btnPanel.add(refreshBtn);

        add(scrollPane, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        loadRequests();

        // Actions
        approveBtn.addActionListener(e -> updateStatus("Approved"));
        rejectBtn.addActionListener(e -> updateStatus("Rejected"));
        refreshBtn.addActionListener(e -> loadRequests());
    }

    private void loadRequests() {
        model.setRowCount(0);

        for (LeaveRequest req : leaveService.getAllRequests()) {
            Employee emp = (employeeService != null) ? employeeService.getEmployeeById(req.getEmployeeId()) : null;

            model.addRow(new Object[]{
                    req.getId(),
                    req.getEmployeeId(),
                    emp != null ? emp.getName() : "Unknown",
                    req.getStartDate(),
                    req.getEndDate(),
                    req.getStatus()
            });
        }
    }

    private void updateStatus(String newStatus) {
        int selected = table.getSelectedRow();

        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Please select a leave request.");
            return;
        }

        int requestId = (int) model.getValueAt(selected, 0);

        LeaveRequest req = leaveService.getRequestById(requestId);
        if (req == null) {
            JOptionPane.showMessageDialog(this, "Selected request not found.");
            return;
        }

        // only allow status change if pending (avoid flip-flop)
        if (!"Pending".equalsIgnoreCase(req.getStatus())) {
            JOptionPane.showMessageDialog(this, "Only Pending requests can be changed.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Change status to " + newStatus + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        req.setStatus(newStatus);

        leaveService.updateRequest(req);

        loadRequests();
        JOptionPane.showMessageDialog(this, "Request updated to: " + newStatus);
    }
}

