package ui;

import models.Employee;
import services.EmployeeService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * ManageEmployeesGUI - keep original flow, added input validation and guards.
 */
public class ManageEmployeesGUI extends JFrame {

    private final EmployeeService employeeService;
    private final JTable table;
    private final DefaultTableModel tableModel;

    public ManageEmployeesGUI(EmployeeService employeeService) {
        this.employeeService = employeeService;

        setTitle("Manage Employees");
        setSize(800, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columns = {"ID", "Name", "Department", "Job Title", "Salary"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton refreshBtn = new JButton("Refresh");

        btnPanel.add(addBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(refreshBtn);

        add(btnPanel, BorderLayout.SOUTH);

        // actions
        addBtn.addActionListener(e -> addEmployeeDialog());
        updateBtn.addActionListener(e -> updateEmployeeDialog());
        deleteBtn.addActionListener(e -> deleteSelectedEmployee());
        refreshBtn.addActionListener(e -> refreshTable());

        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Employee> list = employeeService.getAllEmployees();
        for (Employee e : list) {
            tableModel.addRow(new Object[]{
                    e.getId(),
                    e.getName(),
                    e.getDepartment(),
                    e.getJobTitle(),
                    e.getSalary()
            });
        }
    }

    private void addEmployeeDialog() {
        try {
            String idStr = JOptionPane.showInputDialog(this, "Enter ID:");
            if (idStr == null) return;
            int id = Integer.parseInt(idStr.trim());
            if (employeeService.getEmployeeById(id) != null) {
                JOptionPane.showMessageDialog(this, "ID already exists!");
                return;
            }
            String name = JOptionPane.showInputDialog(this, "Enter Name:");
            if (name == null || name.trim().isEmpty()) return;
            String dept = JOptionPane.showInputDialog(this, "Enter Department:");
            if (dept == null) dept = "";
            String job = JOptionPane.showInputDialog(this, "Enter Job Title:");
            if (job == null) job = "";
            String salaryStr = JOptionPane.showInputDialog(this, "Enter Salary:");
            if (salaryStr == null) return;
            double salary = Double.parseDouble(salaryStr.trim());
            if (salary < 0) {
                JOptionPane.showMessageDialog(this, "Salary cannot be negative.");
                return;
            }

            Employee e = new Employee(id, name.trim(), dept.trim(), job.trim(), salary);
            boolean ok = employeeService.addEmployee(e);
            if (!ok) {
                JOptionPane.showMessageDialog(this, "Add failed. Check duplicate ID.");
                return;
            }
            refreshTable();
            JOptionPane.showMessageDialog(this, "Employee added.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number input.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateEmployeeDialog() {
        try {
            int selected = table.getSelectedRow();
            if (selected == -1) {
                JOptionPane.showMessageDialog(this, "Select an employee row first!");
                return;
            }

            int id = (int) tableModel.getValueAt(selected, 0);

            String name = JOptionPane.showInputDialog(this, "Enter New Name:", tableModel.getValueAt(selected,1));
            if (name == null || name.trim().isEmpty()) return;
            String dept = JOptionPane.showInputDialog(this, "Enter New Department:", tableModel.getValueAt(selected,2));
            if (dept == null) dept = "";
            String job = JOptionPane.showInputDialog(this, "Enter New Job Title:", tableModel.getValueAt(selected,3));
            if (job == null) job = "";
            String salaryStr = JOptionPane.showInputDialog(this, "Enter New Salary:", tableModel.getValueAt(selected,4));
            if (salaryStr == null) return;
            double salary = Double.parseDouble(salaryStr.trim());
            if (salary < 0) {
                JOptionPane.showMessageDialog(this, "Salary cannot be negative.");
                return;
            }

            Employee updated = new Employee(id, name.trim(), dept.trim(), job.trim(), salary);
            boolean ok = employeeService.updateEmployee(id, updated);
            if (ok) {
                refreshTable();
                JOptionPane.showMessageDialog(this, "Employee updated.");
            } else {
                JOptionPane.showMessageDialog(this, "Update failed. Employee not found.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number input.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedEmployee() {
        try {
            int selected = table.getSelectedRow();
            if (selected == -1) {
                JOptionPane.showMessageDialog(this, "Select an employee row first!");
                return;
            }
            int id = (int) tableModel.getValueAt(selected, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Delete employee ID " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean removed = employeeService.deleteEmployee(id);
                if (removed) {
                    refreshTable();
                    JOptionPane.showMessageDialog(this, "Employee deleted.");
                } else {
                    JOptionPane.showMessageDialog(this, "Deletion failed.");
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}



