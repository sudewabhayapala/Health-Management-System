package view;

import controller.HealthcareController;
import model.Prescription;
import model.Patient;
import model.Clinician;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel for managing prescriptions
 */
public class PrescriptionPanel extends JPanel {
    private HealthcareController controller;
    private JTable prescriptionTable;
    private DefaultTableModel tableModel;

    public PrescriptionPanel(HealthcareController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        initializeComponents();
    }

    private void initializeComponents() {
        // Title
        JLabel titleLabel = new JLabel("Prescription Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);
        
        // Table
        String[] columnNames = {"Prescription ID", "Patient", "Clinician", "Date", "Condition", "Drug", "Dosage"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        prescriptionTable = new JTable(tableModel);
        prescriptionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        prescriptionTable.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(prescriptionTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshButton = new JButton("Refresh");
        JButton createButton = new JButton("Create Prescription");
        JButton viewDetailsButton = new JButton("View Details");
        
        refreshButton.addActionListener(e -> refreshData());
        createButton.addActionListener(e -> createPrescription());
        viewDetailsButton.addActionListener(e -> viewPrescriptionDetails());
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(createButton);
        buttonPanel.add(viewDetailsButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        List<Prescription> prescriptions = controller.getAllPrescriptions();
        
        for (Prescription pres : prescriptions) {
            Patient patient = controller.getPatientById(pres.getPatientId());
            Clinician clinician = controller.getClinicianById(pres.getClinicianId());
            
            Object[] row = {
                pres.getPrescriptionId(),
                patient != null ? patient.getFullName() : pres.getPatientId(),
                clinician != null ? clinician.getFullName() : pres.getClinicianId(),
                pres.getPrescriptionDate(),
                pres.getCondition(),
                pres.getDrugName(),
                pres.getDosage()
            };
            tableModel.addRow(row);
        }
    }

    private void createPrescription() {
        JPanel panel = new JPanel(new GridLayout(8, 2, 5, 5));
        
        JComboBox<String> patientCombo = new JComboBox<>();
        for (Patient p : controller.getAllPatients()) {
            patientCombo.addItem(p.getPatientId() + " - " + p.getFullName());
        }
        
        JComboBox<String> clinicianCombo = new JComboBox<>();
        List<Clinician> gps = controller.getCliniciansByType("GP");
        for (Clinician c : gps) {
            clinicianCombo.addItem(c.getClinicianId() + " - " + c.getFullName());
        }
        
        JTextField conditionField = new JTextField();
        JTextField drugField = new JTextField();
        JTextField dosageField = new JTextField();
        JTextField durationField = new JTextField();
        JTextArea instructionsArea = new JTextArea(3, 20);
        
        panel.add(new JLabel("Patient:"));
        panel.add(patientCombo);
        panel.add(new JLabel("Clinician (GP):"));
        panel.add(clinicianCombo);
        panel.add(new JLabel("Condition:"));
        panel.add(conditionField);
        panel.add(new JLabel("Drug Name:"));
        panel.add(drugField);
        panel.add(new JLabel("Dosage:"));
        panel.add(dosageField);
        panel.add(new JLabel("Duration:"));
        panel.add(durationField);
        panel.add(new JLabel("Instructions:"));
        panel.add(new JScrollPane(instructionsArea));
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Create Prescription", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                String patientId = ((String) patientCombo.getSelectedItem()).split(" - ")[0];
                String clinicianId = ((String) clinicianCombo.getSelectedItem()).split(" - ")[0];
                String condition = conditionField.getText();
                String drug = drugField.getText();
                String dosage = dosageField.getText();
                String duration = durationField.getText();
                String instructions = instructionsArea.getText();
                
                if (condition.isEmpty() || drug.isEmpty() || dosage.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill in all required fields.", 
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                controller.createPrescription(patientId, clinicianId, condition, drug, 
                    dosage, duration, instructions);
                refreshData();
                JOptionPane.showMessageDialog(this, "Prescription created and saved successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error creating prescription: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void viewPrescriptionDetails() {
        int selectedRow = prescriptionTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a prescription to view details.", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String prescriptionId = (String) tableModel.getValueAt(selectedRow, 0);
        
        // Find the prescription
        Prescription prescription = null;
        for (Prescription p : controller.getAllPrescriptions()) {
            if (p.getPrescriptionId().equals(prescriptionId)) {
                prescription = p;
                break;
            }
        }
        
        if (prescription != null) {
            Patient patient = controller.getPatientById(prescription.getPatientId());
            Clinician clinician = controller.getClinicianById(prescription.getClinicianId());
            
            String details = String.format(
                "Prescription Details:\n\n" +
                "Prescription ID: %s\n" +
                "Date: %s\n\n" +
                "Patient: %s (NHS: %s)\n" +
                "Prescribed by: Dr. %s\n\n" +
                "Condition: %s\n" +
                "Drug: %s\n" +
                "Dosage: %s\n" +
                "Duration: %s\n\n" +
                "Instructions:\n%s",
                prescription.getPrescriptionId(),
                prescription.getPrescriptionDate(),
                patient != null ? patient.getFullName() : prescription.getPatientId(),
                patient != null ? patient.getNhsNumber() : "N/A",
                clinician != null ? clinician.getFullName() : prescription.getClinicianId(),
                prescription.getCondition(),
                prescription.getDrugName(),
                prescription.getDosage(),
                prescription.getDuration(),
                prescription.getInstructions()
            );
            
            JTextArea textArea = new JTextArea(details);
            textArea.setEditable(false);
            textArea.setCaretPosition(0);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 400));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Prescription Details", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
