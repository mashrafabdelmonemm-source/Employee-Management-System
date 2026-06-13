package services;

import models.Department;
import storage.IStorage;

import java.util.ArrayList;
import java.util.List;

/**
 * DepartmentService - small fixes:
 * - null-safety on load
 * - prevent duplicate ID and duplicate names (case-insensitive)
 * - generateNewId kept
 */
public class DepartmentService {

    private final IStorage<Department> storage;
    private final List<Department> departments;

    public DepartmentService(IStorage<Department> storage) {
        this.storage = storage;
        List<Department> loaded = storage.load();
        this.departments = (loaded != null) ? loaded : new ArrayList<>();
    }

    public boolean addDepartment(Department department) {
        if (department == null) return false;
        // check duplicate id or name
        if (getDepartmentById(department.getId()) != null) return false;
        for (Department d : departments) {
            if (d.getName().equalsIgnoreCase(department.getName())) return false;
        }
        departments.add(department);
        storage.save(departments);
        return true;
    }

    public List<Department> getAllDepartments() {
        return new ArrayList<>(departments);
    }

    public Department getDepartmentById(int id) {
        for (Department d : departments) if (d.getId() == id) return d;
        return null;
    }

    public boolean updateDepartment(Department updated) {
        if (updated == null) return false;
        for (int i = 0; i < departments.size(); i++) {
            if (departments.get(i).getId() == updated.getId()) {
                departments.set(i, updated);
                storage.save(departments);
                return true;
            }
        }
        return false;
    }

    public boolean deleteDepartment(int id) {
        boolean removed = departments.removeIf(d -> d.getId() == id);
        if (removed) storage.save(departments);
        return removed;
    }

    // generate new id (simple incremental)
    public int generateNewId() {
        int max = 0;
        for (Department d : departments) if (d.getId() > max) max = d.getId();
        return max + 1;
    }
}


