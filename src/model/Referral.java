package model;

import java.time.LocalDate;

/**
 * Referral entity representing referrals from GP to specialist
 */
public class Referral {
    private String referralId;
    private String patientId;
    private String gpId;
    private String specialistId;
    private LocalDate referralDate;
    private String reason;
    private String urgency; // ROUTINE, URGENT, EMERGENCY
    private String status; // PENDING, ACCEPTED, COMPLETED, DECLINED
    private String notes;

    public Referral(String referralId, String patientId, String gpId, String specialistId, 
                    LocalDate referralDate, String reason, String urgency, String status, String notes) {
        this.referralId = referralId;
        this.patientId = patientId;
        this.gpId = gpId;
        this.specialistId = specialistId;
        this.referralDate = referralDate;
        this.reason = reason;
        this.urgency = urgency;
        this.status = status;
        this.notes = notes;
    }

    // Getters and Setters
    public String getReferralId() {
        return referralId;
    }

    public void setReferralId(String referralId) {
        this.referralId = referralId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getGpId() {
        return gpId;
    }

    public void setGpId(String gpId) {
        this.gpId = gpId;
    }

    public String getSpecialistId() {
        return specialistId;
    }

    public void setSpecialistId(String specialistId) {
        this.specialistId = specialistId;
    }

    public LocalDate getReferralDate() {
        return referralDate;
    }

    public void setReferralDate(LocalDate referralDate) {
        this.referralDate = referralDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
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

    public String toCSV() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s", 
            referralId, patientId, gpId, specialistId, referralDate, 
            reason, urgency, status, notes);
    }

    @Override
    public String toString() {
        return String.format("Referral %s: Patient %s (%s) - %s", referralId, patientId, urgency, status);
    }
}
