package ui;

import javax.swing.*;
import java.awt.*;

/**
 * MainMenu kept for debug/quick access. Added safety checks and message to discourage use in production.
 */
public class MainMenu extends JFrame {

    public MainMenu() {
        setTitle("EMS - Main Menu");
        setSize(380, 260);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4,1,10,10));

        JButton adminBtn = new JButton("Open Admin Dashboard");
        JButton empBtn = new JButton("Open Employee Dashboard (by ID)");
        JButton loginBtn = new JButton("Open Login");

        add(new JLabel("Main Menu (for debugging)", SwingConstants.CENTER));
        add(adminBtn);
        add(empBtn);
        add(loginBtn);

        adminBtn.addActionListener(e -> new AdminDashboard().setVisible(true));

        empBtn.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog(this, "Enter Employee ID:");
            try {
                if (idStr == null) return;
                int id = Integer.parseInt(idStr.trim());
                if (Main.employeeService == null) {
                    JOptionPane.showMessageDialog(this, "Employee service not initialized.");
                    return;
                }
                var emp = Main.employeeService.getEmployeeById(id);
                if (emp != null) new EmployeeDashboard(emp).setVisible(true);
                else JOptionPane.showMessageDialog(this, "Employee not found.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid ID.");
            }
        });

        loginBtn.addActionListener(e -> new LoginGUI().setVisible(true));
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new MainMenu().setVisible(true));
    }
}

