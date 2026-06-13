package services;

import models.LeaveRequest;
import storage.IStorage;

import java.util.ArrayList;
import java.util.List;

/**
 * LeaveService - fixes:
 * - null-safe load
 * - generateNewId robust
 * - return defensive copies
 */
public class LeaveService {

    private final IStorage<LeaveRequest> storage;
    private List<LeaveRequest> leaves;

    public LeaveService(IStorage<LeaveRequest> storage) {
        this.storage = storage;
        List<LeaveRequest> loaded = storage.load();
        this.leaves = (loaded != null) ? loaded : new ArrayList<>();
    }

    public int generateNewId() {
        return leaves.stream().mapToInt(LeaveRequest::getId).max().orElse(0) + 1;
    }

    public boolean addLeave(LeaveRequest req) {
        if (req == null) return false;
        leaves.add(req);
        storage.save(leaves);
        return true;
    }

    //  GUI بيطلبها
    public LeaveRequest getRequestById(int id) {
        return leaves.stream()
                .filter(l -> l.getId() == id)
                .findFirst()
                .orElse(null);
    }

    //  GUI بيطلبها
    public void updateRequest(LeaveRequest req) {
        for (LeaveRequest l : leaves) {
            if (l.getId() == req.getId()) {
                l.setStatus(req.getStatus());
                l.setStartDate(req.getStartDate());
                l.setEndDate(req.getEndDate());
                break;
            }
        }
        storage.save(leaves);
    }

    public void updateStatus(int id, String newStatus) {
        LeaveRequest req = getRequestById(id);
        if (req != null) {
            req.setStatus(newStatus);
            storage.save(leaves);
        }
    }

    public List<LeaveRequest> getAllRequests() {
        return new ArrayList<>(leaves);
    }

    public List<LeaveRequest> getRequestsForEmployee(int empId) {
        List<LeaveRequest> out = new ArrayList<>();
        for (LeaveRequest l : leaves) if (l.getEmployeeId() == empId) out.add(l);
        return out;
    }
}


