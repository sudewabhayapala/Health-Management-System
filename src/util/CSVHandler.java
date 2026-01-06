package util;

import model.*;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for reading and writing CSV files
 */
public class CSVHandler {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Reads patients from CSV file
     */
    public static List<Patient> readPatients(String filename) {
        List<Patient> patients = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header
                }
                String[] values = line.split(",");
                if (values.length >= 9) {
                    Patient patient = new Patient(
                        values[0].trim(), // patientId
                        values[1].trim(), // firstName
                        values[2].trim(), // lastName
                        values[3].trim(), // email
                        values[4].trim(), // phone
                        LocalDate.parse(values[5].trim(), DATE_FORMATTER), // dateOfBirth
                        values[6].trim(), // address
                        values[7].trim(), // nhsNumber
                        values[8].trim()  // gpId
                    );
                    patients.add(patient);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading patients file: " + e.getMessage());
        }
        return patients;
    }

    /**
     * Reads clinicians from CSV file
     */
    public static List<Clinician> readClinicians(String filename) {
        List<Clinician> clinicians = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header
                }
                String[] values = line.split(",");
                if (values.length >= 8) {
                    Clinician clinician = new Clinician(
                        values[0].trim(), // clinicianId
                        values[1].trim(), // firstName
                        values[2].trim(), // lastName
                        values[3].trim(), // email
                        values[4].trim(), // phone
                        values[5].trim(), // specialty
                        values[6].trim(), // licenseNumber
                        values[7].trim()  // clinicianType
                    );
                    clinicians.add(clinician);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading clinicians file: " + e.getMessage());
        }
        return clinicians;
    }

    /**
     * Reads admin staff from CSV file
     */
    public static List<AdminStaff> readAdminStaff(String filename) {
        List<AdminStaff> staffList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header
                }
                String[] values = line.split(",");
                if (values.length >= 7) {
                    AdminStaff staff = new AdminStaff(
                        values[0].trim(), // staffId
                        values[1].trim(), // firstName
                        values[2].trim(), // lastName
                        values[3].trim(), // email
                        values[4].trim(), // phone
                        values[5].trim(), // role
                        values[6].trim()  // department
                    );
                    staffList.add(staff);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading admin staff file: " + e.getMessage());
        }
        return staffList;
    }

    /**
     * Reads appointments from CSV file
     */
    public static List<Appointment> readAppointments(String filename) {
        List<Appointment> appointments = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header
                }
                String[] values = line.split(",");
                if (values.length >= 7) {
                    Appointment appointment = new Appointment(
                        values[0].trim(), // appointmentId
                        values[1].trim(), // patientId
                        values[2].trim(), // clinicianId
                        LocalDateTime.parse(values[3].trim(), DATETIME_FORMATTER), // appointmentDateTime
                        values[4].trim(), // appointmentType
                        values[5].trim(), // status
                        values[6].trim()  // notes
                    );
                    appointments.add(appointment);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading appointments file: " + e.getMessage());
        }
        return appointments;
    }

    /**
     * Reads prescriptions from CSV file
     */
    public static List<Prescription> readPrescriptions(String filename) {
        List<Prescription> prescriptions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header
                }
                String[] values = line.split(",");
                if (values.length >= 9) {
                    Prescription prescription = new Prescription(
                        values[0].trim(), // prescriptionId
                        values[1].trim(), // patientId
                        values[2].trim(), // clinicianId
                        LocalDate.parse(values[3].trim(), DATE_FORMATTER), // prescriptionDate
                        values[4].trim(), // condition
                        values[5].trim(), // drugName
                        values[6].trim(), // dosage
                        values[7].trim(), // duration
                        values[8].trim()  // instructions
                    );
                    prescriptions.add(prescription);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading prescriptions file: " + e.getMessage());
        }
        return prescriptions;
    }

    /**
     * Reads referrals from CSV file
     */
    public static List<Referral> readReferrals(String filename) {
        List<Referral> referrals = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header
                }
                String[] values = line.split(",");
                if (values.length >= 9) {
                    Referral referral = new Referral(
                        values[0].trim(), // referralId
                        values[1].trim(), // patientId
                        values[2].trim(), // gpId
                        values[3].trim(), // specialistId
                        LocalDate.parse(values[4].trim(), DATE_FORMATTER), // referralDate
                        values[5].trim(), // reason
                        values[6].trim(), // urgency
                        values[7].trim(), // status
                        values[8].trim()  // notes
                    );
                    referrals.add(referral);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading referrals file: " + e.getMessage());
        }
        return referrals;
    }

    /**
     * Writes prescriptions to CSV file
     */
    public static void writePrescriptions(String filename, List<Prescription> prescriptions) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            // Write header
            bw.write("PrescriptionId,PatientId,ClinicianId,PrescriptionDate,Condition,DrugName,Dosage,Duration,Instructions\n");
            
            // Write data
            for (Prescription prescription : prescriptions) {
                bw.write(prescription.toCSV() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error writing prescriptions file: " + e.getMessage());
        }
    }

    /**
     * Appends a prescription to CSV file
     */
    public static void appendPrescription(String filename, Prescription prescription) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
            bw.write(prescription.toCSV() + "\n");
        } catch (IOException e) {
            System.err.println("Error appending prescription: " + e.getMessage());
        }
    }

    /**
     * Writes referrals to CSV file
     */
    public static void writeReferrals(String filename, List<Referral> referrals) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            // Write header
            bw.write("ReferralId,PatientId,GpId,SpecialistId,ReferralDate,Reason,Urgency,Status,Notes\n");
            
            // Write data
            for (Referral referral : referrals) {
                bw.write(referral.toCSV() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error writing referrals file: " + e.getMessage());
        }
    }

    /**
     * Appends a referral to CSV file
     */
    public static void appendReferral(String filename, Referral referral) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
            bw.write(referral.toCSV() + "\n");
        } catch (IOException e) {
            System.err.println("Error appending referral: " + e.getMessage());
        }
    }

    /**
     * Writes appointments to CSV file
     */
    public static void writeAppointments(String filename, List<Appointment> appointments) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            // Write header
            bw.write("AppointmentId,PatientId,ClinicianId,AppointmentDateTime,AppointmentType,Status,Notes\n");
            
            // Write data
            for (Appointment appointment : appointments) {
                bw.write(String.format("%s,%s,%s,%s,%s,%s,%s\n",
                    appointment.getAppointmentId(),
                    appointment.getPatientId(),
                    appointment.getClinicianId(),
                    appointment.getAppointmentDateTime().format(DATETIME_FORMATTER),
                    appointment.getAppointmentType(),
                    appointment.getStatus(),
                    appointment.getNotes()));
            }
        } catch (IOException e) {
            System.err.println("Error writing appointments file: " + e.getMessage());
        }
    }
}
