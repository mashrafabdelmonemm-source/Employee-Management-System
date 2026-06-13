package ui;

import com.google.gson.reflect.TypeToken;
import models.*;
import services.*;
import storage.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    public static EmployeeService employeeService;
    public static DepartmentService departmentService;
    public static AttendanceService attendanceService;
    public static PayrollService payrollService;
    public static LeaveService leaveService;

    public static void main(String[] args) {

        // تأكد من وجود مجلد data
        try {
            Files.createDirectories(Paths.get("data"));
            ensureFile("data/employees.json");
            ensureFile("data/departments.json");
            ensureFile("data/attendance.json");
            ensureFile("data/payroll.json");
            ensureFile("data/leaves.json");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        IStorage<Employee> empStorage =
                new JsonStorage<>("data/employees.json",
                        new TypeToken<List<Employee>>(){}.getType());

        IStorage<Department> deptStorage =
                new JsonStorage<>("data/departments.json",
                        new TypeToken<List<Department>>(){}.getType());

        IStorage<AttendanceRecord> attStorage =
                new JsonStorage<>("data/attendance.json",
                        new TypeToken<List<AttendanceRecord>>(){}.getType());

        IStorage<Payroll> payStorage =
                new JsonStorage<>("data/payroll.json",
                        new TypeToken<List<Payroll>>(){}.getType());

        IStorage<LeaveRequest> leaveStorage =
                new JsonStorage<>("data/leaves.json",
                        new TypeToken<List<LeaveRequest>>(){}.getType());


        // Services
        employeeService = new EmployeeService(empStorage);
        departmentService = new DepartmentService(deptStorage);
        attendanceService = new AttendanceService(attStorage);
        payrollService = new PayrollService(payStorage);
        leaveService = new LeaveService(leaveStorage);

        // Start GUI
        javax.swing.SwingUtilities.invokeLater(() -> new LoginGUI().setVisible(true));
    }

    private static void ensureFile(String path) throws Exception {
        if (!Files.exists(Paths.get(path))) {
            Files.write(Paths.get(path), "[]".getBytes());
        }
    }
}
