package ui;

import models.Department;
import services.DepartmentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * ManageDepartmentsGUI - keep original UI but added input validation and delete protection notes.
 */
public class ManageDepartmentsGUI extends JFrame {

    private DepartmentService departmentService;
    private JTable table;
    private DefaultTableModel model;

    public ManageDepartmentsGUI(DepartmentService departmentService) {
        this.departmentService = departmentService;

        setTitle("Manage Departments");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Table Model
        model = new DefaultTableModel(new String[]{"ID", "Name", "Description"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Buttons
        JButton addBtn = new JButton("Add Department");
        JButton editBtn = new JButton("Edit Department");
        JButton deleteBtn = new JButton("Delete Department");

        JPanel btnPanel = new JPanel();
        btnPanel.add(addBtn);
        btnPanel.add(editBtn);
        btnPanel.add(deleteBtn);

        add(scrollPane, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        loadDepartments();

        // Action Listeners
        addBtn.addActionListener(e -> openAddDialog());
        editBtn.addActionListener(e -> openEditDialog());
        deleteBtn.addActionListener(e -> deleteDepartment());
    }

    // Load data into table
    private void loadDepartments() {
        model.setRowCount(0);
        for (Department dep : departmentService.getAllDepartments()) {
            model.addRow(new Object[]{dep.getId(), dep.getName(), dep.getDescription()});
        }
    }

    // Add Department Dialog
    private void openAddDialog() {
        JTextField nameField = new JTextField();
        JTextField descriptionField = new JTextField();

        Object[] fields = {
                "Name:", nameField,
                "Description:", descriptionField
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Add Department", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            if (name == null || name.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name cannot be empty!");
                return;
            }
            Department dep = new Department(
                    departmentService.generateNewId(),
                    name.trim(),
                    descriptionField.getText()
            );

            boolean ok = departmentService.addDepartment(dep);
            if (!ok) {
                JOptionPane.showMessageDialog(this, "Add failed: duplicate ID or name.");
                return;
            }
            loadDepartments();
        }
    }

    // Edit Department Dialog
    private void openEditDialog() {
        int selected = table.getSelectedRow();
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Select a department first!");
            return;
        }

        int id = (int) model.getValueAt(selected, 0);
        Department dep = departmentService.getDepartmentById(id);

        JTextField nameField = new JTextField(dep.getName());
        JTextField descriptionField = new JTextField(dep.getDescription());

        Object[] fields = {
                "Name:", nameField,
                "Description:", descriptionField
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Edit Department", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            if (name == null || name.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name cannot be empty!");
                return;
            }
            dep.setName(name.trim());
            dep.setDescription(descriptionField.getText());
            boolean ok = departmentService.updateDepartment(dep);
            if (!ok) {
                JOptionPane.showMessageDialog(this, "Update failed.");
                return;
            }
            loadDepartments();
        }
    }

    // Delete Department
    private void deleteDepartment() {
        int selected = table.getSelectedRow();
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Select a department to delete!");
            return;
        }

        int id = (int) model.getValueAt(selected, 0);

        // WARNING: If there are employees assigned to this department, deleting will leave dangling references.
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this department? (Ensure no employees are assigned)",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            boolean removed = departmentService.deleteDepartment(id);
            if (!removed) {
                JOptionPane.showMessageDialog(this, "Delete failed.");
            } else {
                loadDepartments();
            }
        }
    }
}

