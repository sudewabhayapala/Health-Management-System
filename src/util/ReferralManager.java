package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import model.Clinician;
import model.Patient;
import model.Referral;

/**
 * Singleton class for managing referrals in the healthcare system.
 * Ensures single instance for managing referral queues, email communications,
 * and electronic health record updates.
 */
public class ReferralManager {
    private static ReferralManager instance;
    private Queue<Referral> referralQueue;
    private List<Referral> allReferrals;
    private static final String EMAIL_LOG_FILE = "data/email_communications.txt";
    private static final String EHR_LOG_FILE = "data/ehr_updates.txt";

    // Private constructor to prevent instantiation
    private ReferralManager() {
        this.referralQueue = new LinkedList<>();
        this.allReferrals = new ArrayList<>();
    }

    /**
     * Returns the singleton instance of ReferralManager
     */
    public static synchronized ReferralManager getInstance() {
        if (instance == null) {
            instance = new ReferralManager();
        }
        return instance;
    }

    /**
     * Adds a referral to the queue and processes it
     */
    public void addReferral(Referral referral, Patient patient, Clinician gp, Clinician specialist) {
        referralQueue.offer(referral);
        allReferrals.add(referral);
        
        // Process the referral
        processReferral(referral, patient, gp, specialist);
    }

    /**
     * Processes a referral by generating email and updating EHR
     */
    private void processReferral(Referral referral, Patient patient, Clinician gp, Clinician specialist) {
        generateEmailCommunication(referral, patient, gp, specialist);
        updateElectronicHealthRecord(referral, patient);
    }

    /**
     * Generates email communication for a referral
     */
    private void generateEmailCommunication(Referral referral, Patient patient, Clinician gp, Clinician specialist) {
        StringBuilder emailContent = new StringBuilder();
        emailContent.append("=".repeat(80)).append("\n");
        emailContent.append("EMAIL COMMUNICATION - REFERRAL NOTIFICATION\n");
        emailContent.append("=".repeat(80)).append("\n");
        emailContent.append(String.format("Date: %s\n", LocalDateTime.now()));
        emailContent.append(String.format("Referral ID: %s\n\n", referral.getReferralId()));
        
        emailContent.append("FROM:\n");
        emailContent.append(String.format("  Dr. %s\n", gp.getFullName()));
        emailContent.append("  General Practitioner\n");
        emailContent.append(String.format("  Email: %s\n", gp.getEmail()));
        emailContent.append(String.format("  Phone: %s\n\n", gp.getPhone()));
        
        emailContent.append("TO:\n");
        emailContent.append(String.format("  Dr. %s\n", specialist.getFullName()));
        emailContent.append(String.format("  Specialist - %s\n", specialist.getSpecialty()));
        emailContent.append(String.format("  Email: %s\n", specialist.getEmail()));
        emailContent.append(String.format("  Phone: %s\n\n", specialist.getPhone()));
        
        emailContent.append("SUBJECT: Referral for Patient - ").append(patient.getFullName()).append("\n\n");
        
        emailContent.append("PATIENT INFORMATION:\n");
        emailContent.append(String.format("  Name: %s\n", patient.getFullName()));
        emailContent.append(String.format("  NHS Number: %s\n", patient.getNhsNumber()));
        emailContent.append(String.format("  Date of Birth: %s\n", patient.getDateOfBirth()));
        emailContent.append(String.format("  Contact: %s\n", patient.getPhone()));
        emailContent.append(String.format("  Email: %s\n\n", patient.getEmail()));
        
        emailContent.append("REFERRAL DETAILS:\n");
        emailContent.append(String.format("  Referral Date: %s\n", referral.getReferralDate()));
        emailContent.append(String.format("  Urgency: %s\n", referral.getUrgency()));
        emailContent.append(String.format("  Reason for Referral: %s\n", referral.getReason()));
        emailContent.append(String.format("  Current Status: %s\n\n", referral.getStatus()));
        
        if (referral.getNotes() != null && !referral.getNotes().isEmpty()) {
            emailContent.append("CLINICAL NOTES:\n");
            emailContent.append(String.format("  %s\n\n", referral.getNotes()));
        }
        
        emailContent.append("This is an automated referral notification from the Healthcare Management System.\n");
        emailContent.append("Please review the patient's electronic health record for complete medical history.\n");
        emailContent.append("=".repeat(80)).append("\n\n");
        
        writeToFile(EMAIL_LOG_FILE, emailContent.toString());
    }

    /**
     * Updates the electronic health record for a referral
     */
    private void updateElectronicHealthRecord(Referral referral, Patient patient) {
        StringBuilder ehrUpdate = new StringBuilder();
        ehrUpdate.append("=".repeat(80)).append("\n");
        ehrUpdate.append("ELECTRONIC HEALTH RECORD UPDATE\n");
        ehrUpdate.append("=".repeat(80)).append("\n");
        ehrUpdate.append(String.format("Timestamp: %s\n", LocalDateTime.now()));
        ehrUpdate.append(String.format("Update Type: REFERRAL\n\n"));
        
        ehrUpdate.append("PATIENT:\n");
        ehrUpdate.append(String.format("  Patient ID: %s\n", patient.getPatientId()));
        ehrUpdate.append(String.format("  NHS Number: %s\n", patient.getNhsNumber()));
        ehrUpdate.append(String.format("  Name: %s\n\n", patient.getFullName()));
        
        ehrUpdate.append("REFERRAL RECORD:\n");
        ehrUpdate.append(String.format("  Referral ID: %s\n", referral.getReferralId()));
        ehrUpdate.append(String.format("  GP ID: %s\n", referral.getGpId()));
        ehrUpdate.append(String.format("  Specialist ID: %s\n", referral.getSpecialistId()));
        ehrUpdate.append(String.format("  Date: %s\n", referral.getReferralDate()));
        ehrUpdate.append(String.format("  Reason: %s\n", referral.getReason()));
        ehrUpdate.append(String.format("  Urgency Level: %s\n", referral.getUrgency()));
        ehrUpdate.append(String.format("  Status: %s\n", referral.getStatus()));
        
        if (referral.getNotes() != null && !referral.getNotes().isEmpty()) {
            ehrUpdate.append(String.format("  Notes: %s\n", referral.getNotes()));
        }
        
        ehrUpdate.append("\nEHR updated successfully. Audit trail maintained.\n");
        ehrUpdate.append("=".repeat(80)).append("\n\n");
        
        writeToFile(EHR_LOG_FILE, ehrUpdate.toString());
    }

    /**
     * Writes content to a file (appending mode)
     */
    private void writeToFile(String filename, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            System.err.println("Error writing to file " + filename + ": " + e.getMessage());
        }
    }

    /**
     * Gets the next referral from the queue
     */
    public Referral getNextReferral() {
        return referralQueue.poll();
    }

    /**
     * Gets all referrals
     */
    public List<Referral> getAllReferrals() {
        return new ArrayList<>(allReferrals);
    }

    /**
     * Gets referrals for a specific patient
     */
    public List<Referral> getReferralsByPatient(String patientId) {
        List<Referral> patientReferrals = new ArrayList<>();
        for (Referral referral : allReferrals) {
            if (referral.getPatientId().equals(patientId)) {
                patientReferrals.add(referral);
            }
        }
        return patientReferrals;
    }

    /**
     * Gets referrals for a specific specialist
     */
    public List<Referral> getReferralsBySpecialist(String specialistId) {
        List<Referral> specialistReferrals = new ArrayList<>();
        for (Referral referral : allReferrals) {
            if (referral.getSpecialistId().equals(specialistId)) {
                specialistReferrals.add(referral);
            }
        }
        return specialistReferrals;
    }

    /**
     * Updates referral status
     */
    public void updateReferralStatus(String referralId, String newStatus) {
        for (Referral referral : allReferrals) {
            if (referral.getReferralId().equals(referralId)) {
                referral.setStatus(newStatus);
                break;
            }
        }
    }

    /**
     * Gets the size of the referral queue
     */
    public int getQueueSize() {
        return referralQueue.size();
    }

    /**
     * Clears all referrals (for testing purposes)
     */
    public void clearAllReferrals() {
        referralQueue.clear();
        allReferrals.clear();
    }
}
