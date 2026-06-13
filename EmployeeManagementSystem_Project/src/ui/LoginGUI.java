

package ui;

import models.Employee;

import javax.swing.*;
import java.awt.*;

/**
 * LoginGUI
 * - Admin & Employee login from employees.json
 * - No hardcoded passwords
 * - Uses EmployeeService.login()
 */
public class LoginGUI extends JFrame {

    public LoginGUI() {
        setTitle("Login");
        setSize(380, 260);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // ===== Title =====
        JLabel title = new JLabel("Employee Management System", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        // ===== Form =====
        JPanel form = new JPanel(new GridLayout(4, 1, 8, 8));

        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();

        form.add(new JLabel("Username / Employee ID:"));
        form.add(userField);
        form.add(new JLabel("Password:"));
        form.add(passField);

        add(form, BorderLayout.CENTER);

        // ===== Button =====
        JButton loginBtn = new JButton("Login");
        add(loginBtn, BorderLayout.SOUTH);

        // ===== Action =====
        loginBtn.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please enter username and password",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // check service
            if (Main.employeeService == null) {
                JOptionPane.showMessageDialog(this,
                        "System not initialized!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // LOGIN FROM JSON
            Employee emp = Main.employeeService.login(username, password);

            if (emp == null) {
                JOptionPane.showMessageDialog(this,
                        "Invalid username or password",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // SUCCESS
            JOptionPane.showMessageDialog(this,
                    "Welcome " + emp.getName(),
                    "Login Successful",
                    JOptionPane.INFORMATION_MESSAGE);

            if (emp.isAdmin()) {
                new AdminDashboard().setVisible(true);
            } else {
                new EmployeeDashboard(emp).setVisible(true);
            }

            this.dispose();
        });
    }
}
