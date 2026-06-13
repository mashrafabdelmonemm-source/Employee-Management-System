package services;

import models.Employee;
import storage.IStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * EmployeeService - small but important fixes:
 * - Ensure storage.load() null-safety
 * - Prevent adding duplicate IDs
 * - Provide existsById helper
 */
public class EmployeeService {

    private final IStorage<Employee> storage;
    private final List<Employee> employees;

    public EmployeeService(IStorage<Employee> storage) {
        this.storage = storage;
        List<Employee> loaded = storage.load();
        this.employees = (loaded != null) ? loaded : new ArrayList<>();
    }

    // إضافة موظف جديد
    public boolean addEmployee(Employee employee) {
        if (employee == null) return false;
        // validation: id unique
        if (getEmployeeById(employee.getId()) != null) {
            return false; // duplicate id
        }
        employees.add(employee);
        storage.save(employees);
        return true;
    }

    // عرض كل الموظفين
    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees);
    }

    // البحث عن موظف بال ID
    public Employee getEmployeeById(int id) {
        for (Employee e : employees) {
            if (e.getId() == id) return e;
        }
        return null;
    }

    public boolean exists(int id) {
        return getEmployeeById(id) != null;
    }

    // تعديل بيانات موظف
    public boolean updateEmployee(int id, Employee updatedEmployee) {
        if (updatedEmployee == null) return false;
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getId() == id) {
                // keep id stable
                updatedEmployee.setId(id);
                employees.set(i, updatedEmployee);
                storage.save(employees);
                return true;
            }
        }
        return false;
    }

    // حذف موظف
    public boolean deleteEmployee(int id) {
        boolean removed = employees.removeIf(e -> e.getId() == id);
        if (removed) {
            storage.save(employees);
        }
        return removed;
    }

    // helper: generate new id (optional)
    public int generateNewId() {
        int max = 0;
        for (Employee e : employees) if (e.getId() > max) max = e.getId();
        return max + 1;
    }
// ----------إضافة login حقيقي
    public Employee login(String username, String password) {
    for (Employee e : employees) {
        if (e.getUsername().equals(username)
                && e.getPassword().equals(password)) {
            return e;
        }
    }
    return null;
}

}





