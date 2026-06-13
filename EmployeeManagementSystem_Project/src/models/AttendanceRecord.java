package models;

import java.util.Objects;

/**
 * AttendanceRecord - small improvements:
 * - keep strings for compatibility with existing JSON, but validate format in service.
 * - totalHours kept as double (hours), check calcs in AttendanceService.
 */
public class AttendanceRecord {
    private int employeeId;
    private String date;
    private String checkInTime;
    private String checkOutTime;
    private double totalHours;

    public AttendanceRecord() {}

    public AttendanceRecord(int employeeId, String date, String checkInTime) {
        this.employeeId = employeeId;
        this.date = (date == null) ? "" : date.trim();
        this.checkInTime = (checkInTime == null) ? "" : checkInTime.trim();
        this.checkOutTime = null;
        this.totalHours = 0;
    }

    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = (date == null) ? "" : date.trim(); }

    public String getCheckInTime() { return checkInTime; }
    public void setCheckInTime(String checkInTime) { this.checkInTime = (checkInTime == null) ? "" : checkInTime.trim(); }

    public String getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(String checkOutTime) { this.checkOutTime = (checkOutTime == null) ? "" : checkOutTime.trim(); }

    public double getTotalHours() { return totalHours; }
    public void setTotalHours(double totalHours) { this.totalHours = (Double.isNaN(totalHours) || Double.isInfinite(totalHours)) ? 0 : totalHours; }

    @Override
    public String toString() {
        return "AttendanceRecord{" +
                "employeeId=" + employeeId +
                ", date='" + date + '\'' +
                ", checkInTime='" + checkInTime + '\'' +
                ", checkOutTime='" + checkOutTime + '\'' +
                ", totalHours=" + totalHours +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AttendanceRecord)) return false;
        AttendanceRecord that = (AttendanceRecord) o;
        return employeeId == that.employeeId &&
                Objects.equals(date, that.date) &&
                Objects.equals(checkInTime, that.checkInTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, date, checkInTime);
    }
}

