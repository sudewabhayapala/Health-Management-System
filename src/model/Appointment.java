package model;

import java.time.LocalDateTime;

/**
 * Appointment entity representing appointments in the healthcare system
 */
public class Appointment {
    private String appointmentId;
    private String patientId;
    private String clinicianId;
    private LocalDateTime appointmentDateTime;
    private String appointmentType; // CONSULTATION, FOLLOW_UP, EMERGENCY
    private String status; // SCHEDULED, COMPLETED, CANCELLED
    private String notes;

    public Appointment(String appointmentId, String patientId, String clinicianId, 
                       LocalDateTime appointmentDateTime, String appointmentType, String status, String notes) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.clinicianId = clinicianId;
        this.appointmentDateTime = appointmentDateTime;
        this.appointmentType = appointmentType;
        this.status = status;
        this.notes = notes;
    }

    // Getters and Setters
    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getClinicianId() {
        return clinicianId;
    }

    public void setClinicianId(String clinicianId) {
        this.clinicianId = clinicianId;
    }

    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return String.format("Appointment %s: %s - %s (%s)", appointmentId, appointmentDateTime, appointmentType, status);
    }
}
