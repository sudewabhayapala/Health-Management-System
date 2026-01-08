package controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import model.*;
import util.CSVHandler;
import util.ReferralManager;

/**
 * Main controller for the Healthcare Management System
 * Implements MVC pattern - handles business logic and data management
 */
public class HealthcareController {
    private List<Patient> patients;
    private List<Clinician> clinicians;
    private List<AdminStaff> adminStaff;
    private List<Appointment> appointments;
    private List<Prescription> prescriptions;
    private ReferralManager referralManager;
    
    private int nextAppointmentId = 1000;
    private int nextPrescriptionId = 2000;
    private int nextReferralId = 3000;
    private int nextPatientId = 1000;

    public HealthcareController() {
        this.patients = new ArrayList<>();
        this.clinicians = new ArrayList<>();
        this.adminStaff = new ArrayList<>();
        this.appointments = new ArrayList<>();
        this.prescriptions = new ArrayList<>();
        this.referralManager = ReferralManager.getInstance();
    }

    // ==================== Data Loading Methods ====================
    
    public void loadPatients(String filename) {
        patients = CSVHandler.readPatients(filename);
        System.out.println("Loaded " + patients.size() + " patients");
        updateNextPatientId();
    }

    private void updateNextPatientId() {
        for (Patient patient : patients) {
            try {
                int id = Integer.parseInt(patient.getPatientId().replaceAll("[^0-9]", ""));
                if (id >= nextPatientId) {
                    nextPatientId = id + 1;
                }
            } catch (NumberFormatException e) {
                // Skip non-numeric IDs
            }
        }
    }

    public void loadClinicians(String filename) {
        clinicians = CSVHandler.readClinicians(filename);
        System.out.println("Loaded " + clinicians.size() + " clinicians");
    }

    public void loadAdminStaff(String filename) {
        adminStaff = CSVHandler.readAdminStaff(filename);
        System.out.println("Loaded " + adminStaff.size() + " admin staff");
    }

    public void loadAppointments(String filename) {
        appointments = CSVHandler.readAppointments(filename);
        System.out.println("Loaded " + appointments.size() + " appointments");
        updateNextAppointmentId();
    }

    public void loadPrescriptions(String filename) {
        prescriptions = CSVHandler.readPrescriptions(filename);
        System.out.println("Loaded " + prescriptions.size() + " prescriptions");
        updateNextPrescriptionId();
    }

    public void loadReferrals(String filename) {
        List<Referral> referrals = CSVHandler.readReferrals(filename);
        for (Referral referral : referrals) {
            referralManager.getAllReferrals().add(referral);
        }
        System.out.println("Loaded " + referrals.size() + " referrals");
        updateNextReferralId();
    }

    private void updateNextAppointmentId() {
        for (Appointment apt : appointments) {
            try {
                int id = Integer.parseInt(apt.getAppointmentId().replaceAll("[^0-9]", ""));
                if (id >= nextAppointmentId) {
                    nextAppointmentId = id + 1;
                }
            } catch (NumberFormatException e) {
                // Skip non-numeric IDs
            }
        }
    }

    private void updateNextPrescriptionId() {
        for (Prescription pres : prescriptions) {
            try {
                int id = Integer.parseInt(pres.getPrescriptionId().replaceAll("[^0-9]", ""));
                if (id >= nextPrescriptionId) {
                    nextPrescriptionId = id + 1;
                }
            } catch (NumberFormatException e) {
                // Skip non-numeric IDs
            }
        }
    }

    private void updateNextReferralId() {
        for (Referral ref : referralManager.getAllReferrals()) {
            try {
                int id = Integer.parseInt(ref.getReferralId().replaceAll("[^0-9]", ""));
                if (id >= nextReferralId) {
                    nextReferralId = id + 1;
                }
            } catch (NumberFormatException e) {
                // Skip non-numeric IDs
            }
        }
    }

    // ==================== Patient Management ====================
    
    public Patient addPatient(String firstName, String lastName,
                             String email, String phone, String dobString,
                             String address, String nhsNumber, String gpId) {
        // Auto-generate patient ID
        String patientId = "P" + nextPatientId++;
        
        try {
            LocalDate dateOfBirth = LocalDate.parse(dobString);
            Patient patient = new Patient(patientId, firstName, lastName, email, phone,
                                         dateOfBirth, address, nhsNumber, gpId);
            patients.add(patient);
            savePatients();
            return patient;
        } catch (Exception e) {
            System.err.println("Error creating patient: " + e.getMessage());
            return null;
        }
    }

    private void savePatients() {
        CSVHandler.writePatients("data/patients.csv", patients);
    }

    public boolean deletePatient(String patientId) {
        Patient patient = getPatientById(patientId);
        if (patient != null) {
            patients.remove(patient);
            savePatients();
            return true;
        }
        return false;
    }

    // ==================== Appointment Management ====================
    
    public Appointment createAppointment(String patientId, String clinicianId, 
                                        LocalDateTime dateTime, String type, String notes) {
        String appointmentId = "APT" + nextAppointmentId++;
        Appointment appointment = new Appointment(appointmentId, patientId, clinicianId, 
                                                  dateTime, type, "SCHEDULED", notes);
        appointments.add(appointment);
        saveAppointments();
        return appointment;
    }

    public boolean modifyAppointment(String appointmentId, LocalDateTime newDateTime, String newNotes) {
        for (Appointment apt : appointments) {
            if (apt.getAppointmentId().equals(appointmentId)) {
                apt.setAppointmentDateTime(newDateTime);
                apt.setNotes(newNotes);
                saveAppointments();
                return true;
            }
        }
        return false;
    }

    public boolean cancelAppointment(String appointmentId) {
        for (Appointment apt : appointments) {
            if (apt.getAppointmentId().equals(appointmentId)) {
                apt.setStatus("CANCELLED");
                saveAppointments();
                return true;
            }
        }
        return false;
    }

    public List<Appointment> getAppointmentsByPatient(String patientId) {
        return appointments.stream()
                .filter(apt -> apt.getPatientId().equals(patientId))
                .collect(Collectors.toList());
    }

    public List<Appointment> getAppointmentsByClinician(String clinicianId) {
        return appointments.stream()
                .filter(apt -> apt.getClinicianId().equals(clinicianId))
                .collect(Collectors.toList());
    }

    // ==================== Prescription Management ====================
    
    public Prescription createPrescription(String patientId, String clinicianId, String condition,
                                          String drugName, String dosage, String duration, String instructions) {
        String prescriptionId = "PRC" + nextPrescriptionId++;
        Prescription prescription = new Prescription(prescriptionId, patientId, clinicianId,
                                                     LocalDate.now(), condition, drugName, 
                                                     dosage, duration, instructions);
        prescriptions.add(prescription);
        CSVHandler.appendPrescription("data/prescriptions.csv", prescription);
        return prescription;
    }

    public List<Prescription> getPrescriptionsByPatient(String patientId) {
        return prescriptions.stream()
                .filter(pres -> pres.getPatientId().equals(patientId))
                .collect(Collectors.toList());
    }

    public List<Prescription> getPrescriptionsByClinician(String clinicianId) {
        return prescriptions.stream()
                .filter(pres -> pres.getClinicianId().equals(clinicianId))
                .collect(Collectors.toList());
    }

    // ==================== Referral Management ====================
    
    public Referral createReferral(String patientId, String gpId, String specialistId,
                                   String reason, String urgency, String notes) {
        String referralId = "REF" + nextReferralId++;
        Referral referral = new Referral(referralId, patientId, gpId, specialistId,
                                         LocalDate.now(), reason, urgency, "PENDING", notes);
        
        // Get patient, GP, and specialist objects
        Patient patient = getPatientById(patientId);
        Clinician gp = getClinicianById(gpId);
        Clinician specialist = getClinicianById(specialistId);
        
        // Use singleton ReferralManager to process the referral
        referralManager.addReferral(referral, patient, gp, specialist);
        
        // Save to CSV
        CSVHandler.appendReferral("data/referrals.csv", referral);
        
        return referral;
    }

    public List<Referral> getReferralsByPatient(String patientId) {
        return referralManager.getReferralsByPatient(patientId);
    }

    public List<Referral> getReferralsBySpecialist(String specialistId) {
        return referralManager.getReferralsBySpecialist(specialistId);
    }

    public void updateReferralStatus(String referralId, String newStatus) {
        referralManager.updateReferralStatus(referralId, newStatus);
        saveReferrals();
    }

    // ==================== Getters for Entities ====================
    
    public Patient getPatientById(String patientId) {
        return patients.stream()
                .filter(p -> p.getPatientId().equals(patientId))
                .findFirst()
                .orElse(null);
    }

    public Clinician getClinicianById(String clinicianId) {
        return clinicians.stream()
                .filter(c -> c.getClinicianId().equals(clinicianId))
                .findFirst()
                .orElse(null);
    }

    public List<Patient> getAllPatients() {
        return new ArrayList<>(patients);
    }

    public List<Clinician> getAllClinicians() {
        return new ArrayList<>(clinicians);
    }

    public List<Clinician> getCliniciansByType(String type) {
        return clinicians.stream()
                .filter(c -> c.getClinicianType().equals(type))
                .collect(Collectors.toList());
    }

    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments);
    }

    public List<Prescription> getAllPrescriptions() {
        return new ArrayList<>(prescriptions);
    }

    public List<Referral> getAllReferrals() {
        return referralManager.getAllReferrals();
    }

    // ==================== Save Methods ====================
    
    private void saveAppointments() {
        CSVHandler.writeAppointments("data/appointments.csv", appointments);
    }

    private void saveReferrals() {
        CSVHandler.writeReferrals("data/referrals.csv", referralManager.getAllReferrals());
    }

    public void savePrescriptions() {
        CSVHandler.writePrescriptions("data/prescriptions.csv", prescriptions);
    }
}
