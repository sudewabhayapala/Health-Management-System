package model;

import java.time.LocalDate;

/**
 * Prescription entity representing medical prescriptions
 */
public class Prescription {
    private String prescriptionId;
    private String patientId;
    private String clinicianId;
    private LocalDate prescriptionDate;
    private String condition;
    private String drugName;
    private String dosage;
    private String duration;
    private String instructions;

    public Prescription(String prescriptionId, String patientId, String clinicianId, 
                        LocalDate prescriptionDate, String condition, String drugName, 
                        String dosage, String duration, String instructions) {
        this.prescriptionId = prescriptionId;
        this.patientId = patientId;
        this.clinicianId = clinicianId;
        this.prescriptionDate = prescriptionDate;
        this.condition = condition;
        this.drugName = drugName;
        this.dosage = dosage;
        this.duration = duration;
        this.instructions = instructions;
    }

    // Getters and Setters
    public String getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
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

    public LocalDate getPrescriptionDate() {
        return prescriptionDate;
    }

    public void setPrescriptionDate(LocalDate prescriptionDate) {
        this.prescriptionDate = prescriptionDate;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String toCSV() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s", 
            prescriptionId, patientId, clinicianId, prescriptionDate, 
            condition, drugName, dosage, duration, instructions);
    }

    @Override
    public String toString() {
        return String.format("Prescription %s: %s for %s", prescriptionId, drugName, condition);
    }
}
