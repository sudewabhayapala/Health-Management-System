package model;

import java.time.LocalDate;

/**
 * Patient entity representing a patient in the healthcare system
 */
public class Patient extends User {
    private String patientId;
    private LocalDate dateOfBirth;
    private String address;
    private String nhsNumber;
    private String gpId;

    public Patient(String patientId, String firstName, String lastName, String email, 
                   String phone, LocalDate dateOfBirth, String address, String nhsNumber, String gpId) {
        super(patientId, firstName, lastName, email, phone, "PATIENT");
        this.patientId = patientId;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.nhsNumber = nhsNumber;
        this.gpId = gpId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNhsNumber() {
        return nhsNumber;
    }

    public void setNhsNumber(String nhsNumber) {
        this.nhsNumber = nhsNumber;
    }

    public String getGpId() {
        return gpId;
    }

    public void setGpId(String gpId) {
        this.gpId = gpId;
    }

    @Override
    public String toString() {
        return String.format("Patient: %s (NHS: %s)", getFullName(), nhsNumber);
    }
}
