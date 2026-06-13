package models;

import java.util.Objects;

/**
 * LeaveRequest - kept fields but improved safety on setters.
 * Note: using String dates for backward compatibility with existing JSON.
 * Recommended future improvement: use LocalDate fields and custom serializer.
 */
public class LeaveRequest {

    private int id;
    private int employeeId;
    private String startDate;
    private String endDate;
    private String status; // Pending, Approved, Rejected

    public LeaveRequest() {}

    public LeaveRequest(int id, int employeeId, String startDate, String endDate, String status) {
        this.setId(id);
        this.setEmployeeId(employeeId);
        this.setStartDate(startDate);
        this.setEndDate(endDate);
        this.setStatus(status == null ? "Pending" : status);
    }

    public int getId() { return id; }
    public void setId(int id) {
        if (id < 0) throw new IllegalArgumentException("id must be non-negative");
        this.id = id;
    }

    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = (startDate == null) ? "" : startDate.trim(); }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = (endDate == null) ? "" : endDate.trim(); }

    public String getStatus() { return status; }
    public void setStatus(String status) {
        if (status == null) status = "Pending";
        String s = status.trim();
        if (s.isEmpty()) s = "Pending";
        this.status = s;
    }

    @Override
    public String toString() {
        return id + " - Emp: " + employeeId +
               " From: " + startDate +
               " To: " + endDate +
               " Status: " + status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LeaveRequest)) return false;
        LeaveRequest that = (LeaveRequest) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}



