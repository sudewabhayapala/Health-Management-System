package view;

import controller.HealthcareController;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Patient;

/**
 * Panel for displaying and managing patients
 */
public class PatientPanel extends JPanel {
    private HealthcareController controller;
    private JTable patientTable;
    private DefaultTableModel tableModel;

    public PatientPanel(HealthcareController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        initializeComponents();
    }

    private void initializeComponents() {
        // Title
        JLabel titleLabel = new JLabel("Patient Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);
        
        // Table
        String[] columnNames = {"Patient ID", "Name", "NHS Number", "Date of Birth", "Phone", "Email", "GP ID"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        patientTable = new JTable(tableModel);
        patientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        patientTable.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(patientTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addPatientButton = new JButton("Add Patient");
        JButton deletePatientButton = new JButton("Delete Patient");
        JButton refreshButton = new JButton("Refresh");
        JButton viewDetailsButton = new JButton("View Details");
        
        addPatientButton.addActionListener(e -> addPatient());
        deletePatientButton.addActionListener(e -> deletePatient());
        refreshButton.addActionListener(e -> refreshData());
        viewDetailsButton.addActionListener(e -> viewPatientDetails());
        
        buttonPanel.add(addPatientButton);
        buttonPanel.add(deletePatientButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(viewDetailsButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        List<Patient> patients = controller.getAllPatients();
        
        for (Patient patient : patients) {
            Object[] row = {
                patient.getPatientId(),
                patient.getFullName(),
                patient.getNhsNumber(),
                patient.getDateOfBirth(),
                patient.getPhone(),
                patient.getEmail(),
                patient.getGpId()
            };
            tableModel.addRow(row);
        }
    }

    private void viewPatientDetails() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a patient to view details.", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String patientId = (String) tableModel.getValueAt(selectedRow, 0);
        Patient patient = controller.getPatientById(patientId);
        
        if (patient != null) {
            String details = String.format(
                "Patient Details:\n\n" +
                "Patient ID: %s\n" +
                "Name: %s\n" +
                "NHS Number: %s\n" +
                "Date of Birth: %s\n" +
                "Phone: %s\n" +
                "Email: %s\n" +
                "Address: %s\n" +
                "GP ID: %s",
                patient.getPatientId(),
                patient.getFullName(),
                patient.getNhsNumber(),
                patient.getDateOfBirth(),
                patient.getPhone(),
                patient.getEmail(),
                patient.getAddress(),
                patient.getGpId()
            );
            
            JOptionPane.showMessageDialog(this, details, "Patient Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void addPatient() {
        JPanel panel = new JPanel(new GridLayout(7, 2, 5, 5));
        
        JTextField firstNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();
        
        // Date spinner for date of birth
        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner dobSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dobSpinner, "yyyy-MM-dd");
        dobSpinner.setEditor(dateEditor);
        
        // Set default date to 30 years ago
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -30);
        dobSpinner.setValue(cal.getTime());
        
        JTextField nhsNumberField = new JTextField();
        JTextField gpIdField = new JTextField();
        
        panel.add(new JLabel("First Name:"));
        panel.add(firstNameField);
        panel.add(new JLabel("Last Name:"));
        panel.add(lastNameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Date of Birth:"));
        panel.add(dobSpinner);
        panel.add(new JLabel("NHS Number:"));
        panel.add(nhsNumberField);
        panel.add(new JLabel("GP ID:"));
        panel.add(gpIdField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Patient", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                String firstName = firstNameField.getText().trim();
                String lastName = lastNameField.getText().trim();
                String email = emailField.getText().trim();
                String phone = phoneField.getText().trim();
                
                // Get date from spinner and format it
                Date selectedDate = (Date) dobSpinner.getValue();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String dobString = dateFormat.format(selectedDate);
                
                String nhsNumber = nhsNumberField.getText().trim();
                String gpId = gpIdField.getText().trim();
                
                // Validation
                if (firstName.isEmpty() || lastName.isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "First Name and Last Name are required.", 
                        "Validation Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Create patient
                Patient newPatient = controller.addPatient(firstName, lastName, 
                    email, phone, dobString, "", nhsNumber, gpId);
                
                if (newPatient != null) {
                    JOptionPane.showMessageDialog(this, 
                        "Patient added successfully!\nPatient ID: " + newPatient.getPatientId(), 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                    refreshData();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Failed to add patient. Please try again.", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Error adding patient: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deletePatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a patient to delete.", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String patientId = (String) tableModel.getValueAt(selectedRow, 0);
        String patientName = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete patient:\n" + patientName + " (" + patientId + ")?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = controller.deletePatient(patientId);
            
            if (success) {
                JOptionPane.showMessageDialog(this, 
                    "Patient deleted successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Failed to delete patient. Patient may not exist.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
