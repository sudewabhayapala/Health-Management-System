package view;

import controller.HealthcareController;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Appointment;
import model.Clinician;
import model.Patient;

/**
 * Panel for managing appointments
 */
public class AppointmentPanel extends JPanel {
    private HealthcareController controller;
    private JTable appointmentTable;
    private DefaultTableModel tableModel;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public AppointmentPanel(HealthcareController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        initializeComponents();
    }

    private void initializeComponents() {
        // Title
        JLabel titleLabel = new JLabel("Appointment Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);
        
        // Table
        String[] columnNames = {"Appointment ID", "Patient", "Clinician", "Date & Time", "Type", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        appointmentTable = new JTable(tableModel);
        appointmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        appointmentTable.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(appointmentTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshButton = new JButton("Refresh");
        JButton createButton = new JButton("Create Appointment");
        JButton modifyButton = new JButton("Modify Appointment");
        JButton cancelButton = new JButton("Cancel Appointment");
        
        refreshButton.addActionListener(e -> refreshData());
        createButton.addActionListener(e -> createAppointment());
        modifyButton.addActionListener(e -> modifyAppointment());
        cancelButton.addActionListener(e -> cancelAppointment());
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(createButton);
        buttonPanel.add(modifyButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        List<Appointment> appointments = controller.getAllAppointments();
        
        for (Appointment apt : appointments) {
            Patient patient = controller.getPatientById(apt.getPatientId());
            Clinician clinician = controller.getClinicianById(apt.getClinicianId());
            
            Object[] row = {
                apt.getAppointmentId(),
                patient != null ? patient.getFullName() : apt.getPatientId(),
                clinician != null ? clinician.getFullName() : apt.getClinicianId(),
                apt.getAppointmentDateTime().format(formatter),
                apt.getAppointmentType(),
                apt.getStatus()
            };
            tableModel.addRow(row);
        }
    }

    private void createAppointment() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        
        JComboBox<String> patientCombo = new JComboBox<>();
        for (Patient p : controller.getAllPatients()) {
            patientCombo.addItem(p.getPatientId() + " - " + p.getFullName());
        }
        
        JComboBox<String> clinicianCombo = new JComboBox<>();
        for (Clinician c : controller.getAllClinicians()) {
            clinicianCombo.addItem(c.getClinicianId() + " - " + c.getFullName());
        }
        
        JTextField dateTimeField = new JTextField("2026-01-10 14:00");
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"CONSULTATION", "FOLLOW_UP", "EMERGENCY"});
        JTextArea notesArea = new JTextArea(3, 20);
        
        panel.add(new JLabel("Patient:"));
        panel.add(patientCombo);
        panel.add(new JLabel("Clinician:"));
        panel.add(clinicianCombo);
        panel.add(new JLabel("Date & Time (yyyy-MM-dd HH:mm):"));
        panel.add(dateTimeField);
        panel.add(new JLabel("Type:"));
        panel.add(typeCombo);
        panel.add(new JLabel("Notes:"));
        panel.add(new JScrollPane(notesArea));
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Create Appointment", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                String patientId = ((String) patientCombo.getSelectedItem()).split(" - ")[0];
                String clinicianId = ((String) clinicianCombo.getSelectedItem()).split(" - ")[0];
                LocalDateTime dateTime = LocalDateTime.parse(dateTimeField.getText(), formatter);
                String type = (String) typeCombo.getSelectedItem();
                String notes = notesArea.getText();
                
                controller.createAppointment(patientId, clinicianId, dateTime, type, notes);
                refreshData();
                JOptionPane.showMessageDialog(this, "Appointment created successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error creating appointment: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void modifyAppointment() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an appointment to modify.", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String appointmentId = (String) tableModel.getValueAt(selectedRow, 0);
        String currentDateTime = (String) tableModel.getValueAt(selectedRow, 3);
        
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField dateTimeField = new JTextField(currentDateTime);
        JTextArea notesArea = new JTextArea(3, 20);
        
        panel.add(new JLabel("New Date & Time:"));
        panel.add(dateTimeField);
        panel.add(new JLabel("New Notes:"));
        panel.add(new JScrollPane(notesArea));
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Modify Appointment", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                LocalDateTime newDateTime = LocalDateTime.parse(dateTimeField.getText(), formatter);
                String newNotes = notesArea.getText();
                
                if (controller.modifyAppointment(appointmentId, newDateTime, newNotes)) {
                    refreshData();
                    JOptionPane.showMessageDialog(this, "Appointment modified successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to modify appointment.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cancelAppointment() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an appointment to cancel.", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String appointmentId = (String) tableModel.getValueAt(selectedRow, 0);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to cancel this appointment?", 
            "Confirm Cancellation", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.cancelAppointment(appointmentId)) {
                refreshData();
                JOptionPane.showMessageDialog(this, "Appointment cancelled successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to cancel appointment.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
