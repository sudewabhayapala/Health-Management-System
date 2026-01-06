package view;

import controller.HealthcareController;
import model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

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
        JButton refreshButton = new JButton("Refresh");
        JButton viewDetailsButton = new JButton("View Details");
        
        refreshButton.addActionListener(e -> refreshData());
        viewDetailsButton.addActionListener(e -> viewPatientDetails());
        
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
}
