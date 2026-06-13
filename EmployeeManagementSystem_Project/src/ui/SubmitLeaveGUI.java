package ui;

import models.LeaveRequest;
import models.Employee;
import services.LeaveService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

/**
 * SubmitLeaveGUI - uses LocalDate.parse for validation; keeps strings in LeaveRequest to maintain compatibility.
 */
public class SubmitLeaveGUI extends JFrame {

    private Employee employee;
    private LeaveService leaveService;

    public SubmitLeaveGUI(Employee employee, LeaveService leaveService) {
        this.employee = employee;
        this.leaveService = leaveService;

        setTitle("Submit Leave Request");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(6, 2, 10, 10));

        add(new JLabel("Employee ID:"));
        JTextField empIdField = new JTextField(String.valueOf(employee.getId()));
        empIdField.setEditable(false);
        add(empIdField);

        add(new JLabel("Start Date (YYYY-MM-DD):"));
        JTextField startDateField = new JTextField();
        add(startDateField);

        add(new JLabel("End Date (YYYY-MM-DD):"));
        JTextField endDateField = new JTextField();
        add(endDateField);

        JButton submitBtn = new JButton("Submit Request");
        add(submitBtn);

        JButton cancelBtn = new JButton("Cancel");
        add(cancelBtn);

        submitBtn.addActionListener(e -> {
            try {
                LocalDate sDate = LocalDate.parse(startDateField.getText().trim());
                LocalDate eDate = LocalDate.parse(endDateField.getText().trim());

                if (eDate.isBefore(sDate)) {
                    JOptionPane.showMessageDialog(this, "End date cannot be before start date!");
                    return;
                }

                LeaveRequest req = new LeaveRequest(
                        leaveService.generateNewId(),
                        employee.getId(),
                        sDate.toString(),
                        eDate.toString(),
                        "Pending"
                );

                boolean ok = leaveService.addLeave(req);
                if (!ok) {
                    JOptionPane.showMessageDialog(this, "Failed to submit leave request.");
                    return;
                }
                JOptionPane.showMessageDialog(this, "Leave request submitted!");
                this.dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input! Please try again.");
            }
        });

        cancelBtn.addActionListener(e -> this.dispose());
    }
}



