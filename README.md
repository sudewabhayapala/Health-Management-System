# Health-Management-System

# Healthcare Management System

A comprehensive Java-based healthcare management system implementing the MVC architectural pattern with Singleton design pattern for referral management. Built with Java Swing GUI for managing patients, appointments, prescriptions, and referrals.

## Features

### Core Functionality
- **Patient Management**: View and manage patient records including NHS numbers, contact details, and GP assignments
- **Appointment Management**: Create, modify, and cancel appointments with healthcare providers
- **Prescription Management**: Create prescriptions based on patient symptoms and clinical assessments
- **Referral Management**: Process referrals from GPs to specialists with automated email and EHR updates

### Healthcare Professionals Supported
- **General Practitioners (GPs)**: Primary care providers
- **Specialist Doctors**: Specialized medical expertise (Cardiology, Orthopedics, Neurology, Dermatology, etc.)
- **Nurses**: Clinical support services
- **Administrative Staff**: Receptionists and administrative personnel

## Architecture

### MVC Pattern
The application follows the Model-View-Controller architectural pattern:

- **Model** (`src/model/`): Entity classes representing domain objects
  - `User.java` - Abstract base class for all users
  - `Patient.java` - Patient entity with medical details
  - `Clinician.java` - Healthcare professionals (GP, Specialist, Nurse)
  - `AdminStaff.java` - Administrative and non-clinical staff
  - `Appointment.java` - Appointment records
  - `Prescription.java` - Prescription details
  - `Referral.java` - Referral information

- **View** (`src/view/`): Java Swing GUI components
  - `MainFrame.java` - Main application window with tabbed interface
  - `PatientPanel.java` - Patient management interface
  - `AppointmentPanel.java` - Appointment scheduling and management
  - `PrescriptionPanel.java` - Prescription creation and viewing
  - `ReferralPanel.java` - Referral management with Singleton pattern

- **Controller** (`src/controller/`): Business logic layer
  - `HealthcareController.java` - Central controller managing all operations

### Singleton Pattern
The `ReferralManager` class implements the Singleton design pattern to ensure:
- Single instance creation of the referral management system
- Consistent referral processing across the application
- Prevention of resource conflicts
- Maintenance of data consistency
- Proper audit trails for all referrals

#### Singleton Features
- Email communication generation for referrals
- Electronic Health Record (EHR) updates
- Referral queue management
- Status tracking and updates

## Project Structure

```
healthcare-management-system/
│
├── src/
│   ├── model/              # Domain entities
│   ├── view/               # Swing GUI components
│   ├── controller/         # Business logic
│   └── util/               # Utility classes
│       ├── CSVHandler.java         # CSV file operations
│       └── ReferralManager.java    # Singleton referral manager
│
├── data/                   # CSV data files
│   ├── patients.csv
│   ├── clinicians.csv
│   ├── appointments.csv
│   ├── prescriptions.csv
│   ├── referrals.csv
│   ├── email_communications.txt    # Generated email logs
│   └── ehr_updates.txt            # Generated EHR updates
│
├── bin/                    # Compiled class files
├── .gitignore
└── README.md
```

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- Java Runtime Environment (JRE) 8 or higher

## Installation & Setup

### 1. Clone the Repository
```bash
git clone <repository-url>
cd healthcare-management-system
```

### 2. Compile the Application
```bash
# Create bin directory
mkdir bin

# Compile all Java files in dependency order
javac -source 1.8 -target 1.8 -d bin -cp bin src/model/*.java
javac -source 1.8 -target 1.8 -d bin -cp bin src/util/*.java
javac -source 1.8 -target 1.8 -d bin -cp bin src/controller/*.java
javac -source 1.8 -target 1.8 -d bin -cp bin src/view/*.java
```

Or use the provided compile script:
```bash
# Windows PowerShell
.\compile.bat
```

### 3. Run the Application
```bash
java -cp bin view.MainFrame
```

Or:
```bash
# Windows
.\run.bat
```

## Usage

### Starting the Application
1. Launch the application using the run command above
2. The GUI will open with a tabbed interface
3. Click **File → Load Data** to load sample data from CSV files

### Managing Patients
1. Navigate to the **Patients** tab
2. View all registered patients in the table
3. Select a patient and click **View Details** for complete information

### Managing Appointments
1. Navigate to the **Appointments** tab
2. **Create Appointment**: Select patient, clinician, date/time, and type
3. **Modify Appointment**: Select an appointment and update details
4. **Cancel Appointment**: Select and cancel appointments as needed

### Creating Prescriptions
1. Navigate to the **Prescriptions** tab
2. Click **Create Prescription**
3. Select patient and clinician (GP)
4. Enter condition, drug name, dosage, duration, and instructions
5. Prescription is automatically saved to `data/prescriptions.csv`

### Managing Referrals (Singleton Pattern)
1. Navigate to the **Referrals** tab
2. Click **Create Referral**
3. Select patient, referring GP, and specialist
4. Enter reason for referral, urgency level, and clinical notes
5. The Singleton ReferralManager automatically:
   - Generates email communication → `data/email_communications.txt`
   - Updates electronic health record → `data/ehr_updates.txt`
   - Saves referral to → `data/referrals.csv`
6. **Update Status**: Change referral status (PENDING, ACCEPTED, COMPLETED, DECLINED)

## Data Files

### CSV File Formats

#### patients.csv
```
PatientId,FirstName,LastName,Email,Phone,DateOfBirth,Address,NhsNumber,GpId
```

#### clinicians.csv
```
ClinicianId,FirstName,LastName,Email,Phone,Specialty,LicenseNumber,ClinicianType
```

#### appointments.csv
```
AppointmentId,PatientId,ClinicianId,AppointmentDateTime,AppointmentType,Status,Notes
```

#### prescriptions.csv
```
PrescriptionId,PatientId,ClinicianId,PrescriptionDate,Condition,DrugName,Dosage,Duration,Instructions
```

#### referrals.csv
```
ReferralId,PatientId,GpId,SpecialistId,ReferralDate,Reason,Urgency,Status,Notes
```

## Design Patterns

### 1. Model-View-Controller (MVC)
- **Separation of Concerns**: Business logic, presentation, and data are separated
- **Maintainability**: Easy to modify one layer without affecting others
- **Testability**: Each component can be tested independently

### 2. Singleton Pattern (ReferralManager)
- **Single Instance**: Only one ReferralManager instance exists in the application
- **Global Access Point**: Accessible throughout the application via `getInstance()`
- **Resource Management**: Prevents conflicts in referral processing
- **Consistency**: Ensures all referrals are processed uniformly

```java
// Singleton usage example
ReferralManager manager = ReferralManager.getInstance();
manager.addReferral(referral, patient, gp, specialist);
```

## Key Technical Features

### No External Dependencies
- Pure Java implementation (no frameworks)
- No database technology (CSV-based storage)
- No external libraries beyond Java standard library
- Java Swing for GUI (no JavaFX or other UI frameworks)

### Java 8 Compatibility
- Compiled with `-source 1.8 -target 1.8` flags
- Compatible with JRE 8 and higher
- Uses standard Java 8 features (LocalDate, LocalDateTime, Streams)

### Data Persistence
- CSV files for data storage
- Automatic saving of new prescriptions and referrals
- Modifications to appointments are persisted immediately
- Human-readable data format for easy inspection

## Git Commit History

The project was developed incrementally with regular commits:

1. Initial project structure and git initialization
2. Model classes: User, Patient, Clinician, AdminStaff, Appointment, Prescription, Referral
3. Singleton ReferralManager and CSVHandler utility classes
4. HealthcareController implementing MVC pattern
5. Swing GUI views: MainFrame and all panel classes
6. Sample CSV data files and integration testing

View complete commit history:
```bash
git log --oneline
```

## Testing

### Manual Testing Steps

1. **Load Data**: Verify all CSV files load correctly
2. **View Patients**: Check patient information displays properly
3. **Create Appointment**: Schedule a new appointment
4. **Modify Appointment**: Change appointment date/time
5. **Cancel Appointment**: Cancel an appointment and verify status
6. **Create Prescription**: Generate a prescription and verify CSV file update
7. **Create Referral**: Create a referral and check:
   - Email communication file is generated
   - EHR update file is generated
   - Referral is saved to CSV
8. **Update Referral Status**: Change referral status and verify persistence

## Future Enhancements

- User authentication and role-based access control
- Database integration (MySQL, PostgreSQL)
- Advanced search and filtering capabilities
- Reporting and analytics dashboard
- Email integration for actual email sending
- Appointment reminders and notifications
- Medical history tracking
- Billing and insurance management

## Development Guidelines

### Adding New Features
1. Create model classes in `src/model/`
2. Add business logic to `HealthcareController`
3. Create/update view panels in `src/view/`
4. Update CSV handlers in `util/CSVHandler.java`
5. Test thoroughly and commit changes

### Code Standards
- Follow Java naming conventions
- Document all public methods
- Use meaningful variable names
- Keep methods focused and concise
- Commit regularly with descriptive messages

## Troubleshooting

### "Could not find or load main class"
- Ensure you're running from the correct directory
- Verify classpath includes the `bin` directory
- Check that all files are compiled

### Java Version Mismatch
- Compile with same Java version as runtime
- Use `-source` and `-target` flags if needed

### CSV File Not Found
- Ensure `data/` directory exists
- Verify CSV files are present
- Check file paths in CSVHandler

### GUI Not Displaying
- Verify Java Swing is properly installed
- Check system Look and Feel settings
- Ensure no exceptions in console output

## License

This project is developed as an educational healthcare management system demonstrating MVC architecture and Singleton design pattern implementation.

## Contributors

Developed as part of software engineering coursework demonstrating:
- Object-oriented design principles
- MVC architectural pattern
- Singleton design pattern
- Java Swing GUI development
- CSV-based data persistence
- Git version control

## Support

For issues or questions:
1. Check the troubleshooting section
2. Review the commit history for implementation details
3. Examine the code comments and documentation
4. Test with the provided sample data files

---

**Healthcare Management System v1.0**  
*A comprehensive solution for healthcare delivery management*
