package view;

import controller.HealthcareController;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Clinician;
import model.Patient;
import model.Referral;

/**
 * Panel for managing referrals
 */
public class ReferralPanel extends JPanel {
    private HealthcareController controller;
    private JTable referralTable;
    private DefaultTableModel tableModel;

    public ReferralPanel(HealthcareController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        initializeComponents();
    }

    private void initializeComponents() {
        // Title
        JLabel titleLabel = new JLabel("Referral Management (Singleton Pattern)");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);
        
        // Table
        String[] columnNames = {"Referral ID", "Patient", "GP", "Specialist", "Date", "Reason", "Urgency", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        referralTable = new JTable(tableModel);
        referralTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        referralTable.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(referralTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshButton = new JButton("Refresh");
        JButton createButton = new JButton("Create Referral");
        JButton updateStatusButton = new JButton("Update Status");
        JButton viewDetailsButton = new JButton("View Details");
        
        refreshButton.addActionListener(e -> refreshData());
        createButton.addActionListener(e -> createReferral());
        updateStatusButton.addActionListener(e -> updateReferralStatus());
        viewDetailsButton.addActionListener(e -> viewReferralDetails());
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(createButton);
        buttonPanel.add(updateStatusButton);
        buttonPanel.add(viewDetailsButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        List<Referral> referrals = controller.getAllReferrals();
        
        for (Referral ref : referrals) {
            Patient patient = controller.getPatientById(ref.getPatientId());
            Clinician gp = controller.getClinicianById(ref.getGpId());
            Clinician specialist = controller.getClinicianById(ref.getSpecialistId());
            
            Object[] row = {
                ref.getReferralId(),
                patient != null ? patient.getFullName() : ref.getPatientId(),
                gp != null ? gp.getFullName() : ref.getGpId(),
                specialist != null ? specialist.getFullName() : ref.getSpecialistId(),
                ref.getReferralDate(),
                ref.getReason(),
                ref.getUrgency(),
                ref.getStatus()
            };
            tableModel.addRow(row);
        }
    }

    private void createReferral() {
        JPanel panel = new JPanel(new GridLayout(7, 2, 5, 5));
        
        JComboBox<String> patientCombo = new JComboBox<>();
        for (Patient p : controller.getAllPatients()) {
            patientCombo.addItem(p.getPatientId() + " - " + p.getFullName());
        }
        
        JComboBox<String> gpCombo = new JComboBox<>();
        List<Clinician> gps = controller.getCliniciansByType("GP");
        for (Clinician c : gps) {
            gpCombo.addItem(c.getClinicianId() + " - " + c.getFullName());
        }
        
        JComboBox<String> specialistCombo = new JComboBox<>();
        List<Clinician> specialists = controller.getCliniciansByType("SPECIALIST");
        for (Clinician c : specialists) {
            specialistCombo.addItem(c.getClinicianId() + " - " + c.getFullName() + " (" + c.getSpecialty() + ")");
        }
        
        JTextField reasonField = new JTextField();
        JComboBox<String> urgencyCombo = new JComboBox<>(new String[]{"ROUTINE", "URGENT", "EMERGENCY"});
        JTextArea notesArea = new JTextArea(3, 20);
        
        panel.add(new JLabel("Patient:"));
        panel.add(patientCombo);
        panel.add(new JLabel("Referring GP:"));
        panel.add(gpCombo);
        panel.add(new JLabel("Specialist:"));
        panel.add(specialistCombo);
        panel.add(new JLabel("Reason for Referral:"));
        panel.add(reasonField);
        panel.add(new JLabel("Urgency:"));
        panel.add(urgencyCombo);
        panel.add(new JLabel("Clinical Notes:"));
        panel.add(new JScrollPane(notesArea));
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Create Referral", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                String patientId = ((String) patientCombo.getSelectedItem()).split(" - ")[0];
                String gpId = ((String) gpCombo.getSelectedItem()).split(" - ")[0];
                String specialistId = ((String) specialistCombo.getSelectedItem()).split(" - ")[0];
                String reason = reasonField.getText();
                String urgency = (String) urgencyCombo.getSelectedItem();
                String notes = notesArea.getText();
                
                if (reason.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please provide a reason for referral.", 
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                controller.createReferral(patientId, gpId, specialistId, reason, urgency, notes);
                refreshData();
                
                JOptionPane.showMessageDialog(this, 
                    "Referral created successfully!\n\n" +
                    "Email communication and EHR update have been generated.\n" +
                    "Check data/email_communications.txt and data/ehr_updates.txt for details.",
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error creating referral: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateReferralStatus() {
        int selectedRow = referralTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a referral to update.", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String referralId = (String) tableModel.getValueAt(selectedRow, 0);
        String currentStatus = (String) tableModel.getValueAt(selectedRow, 7);
        
        String[] statuses = {"PENDING", "ACCEPTED", "COMPLETED", "DECLINED"};
        String newStatus = (String) JOptionPane.showInputDialog(this, 
            "Select new status:", 
            "Update Referral Status",
            JOptionPane.QUESTION_MESSAGE,
            null,
            statuses,
            currentStatus);
        
        if (newStatus != null && !newStatus.equals(currentStatus)) {
            controller.updateReferralStatus(referralId, newStatus);
            refreshData();
            JOptionPane.showMessageDialog(this, "Referral status updated successfully!");
        }
    }

    private void viewReferralDetails() {
        int selectedRow = referralTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a referral to view details.", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String referralId = (String) tableModel.getValueAt(selectedRow, 0);
        
        // Find the referral
        Referral referral = null;
        for (Referral r : controller.getAllReferrals()) {
            if (r.getReferralId().equals(referralId)) {
                referral = r;
                break;
            }
        }
        
        if (referral != null) {
            Patient patient = controller.getPatientById(referral.getPatientId());
            Clinician gp = controller.getClinicianById(referral.getGpId());
            Clinician specialist = controller.getClinicianById(referral.getSpecialistId());
            
            String details = String.format(
                "Referral Details:\n\n" +
                "Referral ID: %s\n" +
                "Date: %s\n" +
                "Status: %s\n" +
                "Urgency: %s\n\n" +
                "Patient: %s (NHS: %s)\n" +
                "Referring GP: Dr. %s\n" +
                "Specialist: Dr. %s (%s)\n\n" +
                "Reason for Referral:\n%s\n\n" +
                "Clinical Notes:\n%s\n\n" +
                "Note: This referral was processed using the Singleton ReferralManager.\n" +
                "Email and EHR communications are logged in the data/ directory.",
                referral.getReferralId(),
                referral.getReferralDate(),
                referral.getStatus(),
                referral.getUrgency(),
                patient != null ? patient.getFullName() : referral.getPatientId(),
                patient != null ? patient.getNhsNumber() : "N/A",
                gp != null ? gp.getFullName() : referral.getGpId(),
                specialist != null ? specialist.getFullName() : referral.getSpecialistId(),
                specialist != null ? specialist.getSpecialty() : "N/A",
                referral.getReason(),
                referral.getNotes()
            );
            
            JTextArea textArea = new JTextArea(details);
            textArea.setEditable(false);
            textArea.setCaretPosition(0);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(600, 500));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Referral Details", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
