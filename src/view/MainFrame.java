package view;

import controller.HealthcareController;

import javax.swing.*;
import java.awt.*;

/**
 * Main application window for Healthcare Management System
 * Implements MVC pattern - View component using Java Swing
 */
public class MainFrame extends JFrame {
    private HealthcareController controller;
    private JTabbedPane tabbedPane;
    
    // Panels
    private AppointmentPanel appointmentPanel;
    private PrescriptionPanel prescriptionPanel;
    private ReferralPanel referralPanel;
    private PatientPanel patientPanel;

    public MainFrame(HealthcareController controller) {
        this.controller = controller;
        
        setTitle("Healthcare Management System");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initializeComponents();
        loadData();
    }

    private void initializeComponents() {
        // Create menu bar
        createMenuBar();
        
        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        
        // Create panels
        patientPanel = new PatientPanel(controller);
        appointmentPanel = new AppointmentPanel(controller);
        prescriptionPanel = new PrescriptionPanel(controller);
        referralPanel = new ReferralPanel(controller);
        
        // Add panels to tabbed pane
        tabbedPane.addTab("Patients", new ImageIcon(), patientPanel, "View and manage patients");
        tabbedPane.addTab("Appointments", new ImageIcon(), appointmentPanel, "Manage appointments");
        tabbedPane.addTab("Prescriptions", new ImageIcon(), prescriptionPanel, "Create and view prescriptions");
        tabbedPane.addTab("Referrals", new ImageIcon(), referralPanel, "Manage referrals");
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Add status bar
        JLabel statusBar = new JLabel("Healthcare Management System - Ready");
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        add(statusBar, BorderLayout.SOUTH);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem loadDataItem = new JMenuItem("Load Data");
        JMenuItem exitItem = new JMenuItem("Exit");
        
        loadDataItem.addActionListener(e -> loadData());
        exitItem.addActionListener(e -> System.exit(0));
        
        fileMenu.add(loadDataItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        // Help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAbout());
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }

    private void loadData() {
        try {
            controller.loadPatients("data/patients.csv");
            controller.loadClinicians("data/clinicians.csv");
            controller.loadAppointments("data/appointments.csv");
            controller.loadPrescriptions("data/prescriptions.csv");
            controller.loadReferrals("data/referrals.csv");
            
            // Refresh all panels
            patientPanel.refreshData();
            appointmentPanel.refreshData();
            prescriptionPanel.refreshData();
            referralPanel.refreshData();
            
            JOptionPane.showMessageDialog(this, 
                "Data loaded successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading data: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showAbout() {
        String message = "Healthcare Management System\n" +
                        "Version 1.0\n\n" +
                        "A comprehensive system for managing:\n" +
                        "- Patient records\n" +
                        "- Appointments\n" +
                        "- Prescriptions\n" +
                        "- Referrals\n\n" +
                        "Developed using MVC architecture\n" +
                        "with Singleton pattern for referral management.";
        
        JOptionPane.showMessageDialog(this, message, "About", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        // Set Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Create and show GUI
        SwingUtilities.invokeLater(() -> {
            HealthcareController controller = new HealthcareController();
            MainFrame frame = new MainFrame(controller);
            frame.setVisible(true);
        });
    }
}
