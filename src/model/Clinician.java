package model;

/**
 * Clinician entity representing healthcare professionals (GP, Specialist, Nurse)
 */
public class Clinician extends User {
    private String clinicianId;
    private String specialty;
    private String licenseNumber;
    private String clinicianType; // GP, SPECIALIST, NURSE

    public Clinician(String clinicianId, String firstName, String lastName, String email, 
                     String phone, String specialty, String licenseNumber, String clinicianType) {
        super(clinicianId, firstName, lastName, email, phone, clinicianType);
        this.clinicianId = clinicianId;
        this.specialty = specialty;
        this.licenseNumber = licenseNumber;
        this.clinicianType = clinicianType;
    }

    public String getClinicianId() {
        return clinicianId;
    }

    public void setClinicianId(String clinicianId) {
        this.clinicianId = clinicianId;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getClinicianType() {
        return clinicianType;
    }

    public void setClinicianType(String clinicianType) {
        this.clinicianType = clinicianType;
    }

    @Override
    public String toString() {
        return String.format("%s: %s (%s)", clinicianType, getFullName(), specialty);
    }
}
