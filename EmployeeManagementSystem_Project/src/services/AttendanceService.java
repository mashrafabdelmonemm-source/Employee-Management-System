package services;

import models.AttendanceRecord;
import storage.IStorage;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * AttendanceService - fixes:
 * - protect against storage.load() == null
 * - validate duplicate check-in for same date
 * - safe parsing and negative-duration guard
 */
public class AttendanceService {

    private final IStorage<AttendanceRecord> storage;
    private final List<AttendanceRecord> records;

    public AttendanceService(IStorage<AttendanceRecord> storage) {
        this.storage = storage;
        List<AttendanceRecord> loaded = storage.load();
        this.records = (loaded != null) ? loaded : new ArrayList<>();
    }

    // Check-in by employee id. Uses today's date and current time.
    public boolean checkIn(int employeeId) {
        String today = LocalDate.now().toString();
        // prevent double open record for today
        for (AttendanceRecord r : records) {
            if (r.getEmployeeId() == employeeId && r.getDate().equals(today) && (r.getCheckOutTime() == null || r.getCheckOutTime().isEmpty())) {
                return false;
            }
        }
        AttendanceRecord rec = new AttendanceRecord(employeeId, today, LocalTime.now().toString());
        records.add(rec);
        storage.save(records);
        return true;
    }

    // Check-out by employee id; matches today's open checkin record.
    public boolean checkOut(int employeeId) {
        String today = LocalDate.now().toString();
        for (AttendanceRecord r : records) {
            if (r.getEmployeeId() == employeeId && r.getDate().equals(today) && (r.getCheckOutTime() == null || r.getCheckOutTime().isEmpty())) {
                String outTime = LocalTime.now().toString();
                r.setCheckOutTime(outTime);
                double hours = calculateHours(r.getCheckInTime(), outTime);
                r.setTotalHours(hours);
                storage.save(records);
                return true;
            }
        }
        return false;
    }

    private double calculateHours(String inTimeStr, String outTimeStr) {
        try {
            LocalTime in = LocalTime.parse(inTimeStr);
            LocalTime out = LocalTime.parse(outTimeStr);
            Duration d = Duration.between(in, out);
            if (d.isNegative()) return 0;
            return Math.round((d.toMinutes() / 60.0) * 100.0) / 100.0; // round to 2 decimals
        } catch (Exception e) {
            return 0;
        }
    }

    public List<AttendanceRecord> getAllAttendance() {
        return new ArrayList<>(records);
    }

    public List<AttendanceRecord> getAttendanceForEmployee(int employeeId) {
        List<AttendanceRecord> res = new ArrayList<>();
        for (AttendanceRecord r : records) if (r.getEmployeeId() == employeeId) res.add(r);
        return res;
    }

    // alias
    public List<AttendanceRecord> getAttendanceByEmployee(int employeeId) {
        return getAttendanceForEmployee(employeeId);
    }
}





