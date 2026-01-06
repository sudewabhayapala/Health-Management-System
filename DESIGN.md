# Healthcare Management System - Design Documentation

## System Overview
This healthcare management system is designed to facilitate efficient healthcare delivery through automated management of patients, appointments, prescriptions, and referrals. The system follows professional software engineering practices with clear separation of concerns and proven design patterns.

## Architectural Patterns

### 1. Model-View-Controller (MVC) Pattern

#### Purpose
The MVC pattern separates the application into three interconnected components, promoting organized code and maintainability.

#### Implementation

**Model Layer** (`src/model/`)
- Represents the data and business entities
- No knowledge of presentation or controller logic
- Classes:
  - `User` - Abstract base class for all system users
  - `Patient` - Extends User, adds medical-specific attributes
  - `Clinician` - Healthcare professionals (GP, Specialist, Nurse)
  - `AdminStaff` - Non-clinical staff
  - `Appointment` - Appointment scheduling data
  - `Prescription` - Prescription details
  - `Referral` - Referral information

**View Layer** (`src/view/`)
- Handles all user interface and presentation logic
- Built entirely with Java Swing components
- No direct data manipulation - delegates to Controller
- Classes:
  - `MainFrame` - Main application window with tabbed interface
  - `PatientPanel` - Patient viewing interface
  - `AppointmentPanel` - Appointment management UI
  - `PrescriptionPanel` - Prescription creation UI
  - `ReferralPanel` - Referral management UI

**Controller Layer** (`src/controller/`)
- Mediates between Model and View
- Contains business logic
- Handles data processing and validation
- Classes:
  - `HealthcareController` - Central controller for all operations

#### Benefits
- **Separation of Concerns**: UI changes don't affect business logic
- **Maintainability**: Easy to locate and fix issues
- **Testability**: Each layer can be tested independently
- **Scalability**: New features can be added without major refactoring

### 2. Singleton Design Pattern

#### Purpose
Ensures only one instance of the ReferralManager exists throughout the application lifecycle, preventing resource conflicts and maintaining data consistency.

#### Implementation

**Class**: `ReferralManager` (`src/util/ReferralManager.java`)

```java
public class ReferralManager {
    private static ReferralManager instance;
    private Queue<Referral> referralQueue;
    private List<Referral> allReferrals;
    
    // Private constructor prevents direct instantiation
    private ReferralManager() {
        this.referralQueue = new LinkedList<>();
        this.allReferrals = new ArrayList<>();
    }
    
    // Global access point with lazy initialization
    public static synchronized ReferralManager getInstance() {
        if (instance == null) {
            instance = new ReferralManager();
        }
        return instance;
    }
}
```

#### Key Features
1. **Private Constructor**: Prevents external instantiation
2. **Static Instance Variable**: Holds the single instance
3. **Synchronized getInstance()**: Thread-safe instance creation
4. **Lazy Initialization**: Instance created only when needed

#### Benefits
- **Single Point of Control**: All referrals processed through one manager
- **Resource Management**: Prevents duplicate email/EHR updates
- **Consistent State**: Single queue maintains proper order
- **Audit Trail**: Centralized logging of all referral activities

#### Singleton Responsibilities
1. **Referral Queue Management**: FIFO queue for pending referrals
2. **Email Communication**: Generates referral notification emails
3. **EHR Updates**: Creates electronic health record entries
4. **Status Tracking**: Maintains referral lifecycle states
5. **File Logging**: Persists communications for audit purposes

## Class Diagram Structure

### Entity Hierarchy
```
User (abstract)
├── Patient
├── Clinician (GP, SPECIALIST, NURSE)
└── AdminStaff (RECEPTIONIST, ADMIN)
```

### Key Relationships
- Patient → Clinician (many-to-one): Patient assigned to GP
- Appointment → Patient (many-to-one): Multiple appointments per patient
- Appointment → Clinician (many-to-one): Clinician conducts appointments
- Prescription → Patient (many-to-one): Patient receives prescriptions
- Prescription → Clinician (many-to-one): Clinician creates prescriptions
- Referral → Patient (many-to-one): Patient receives referrals
- Referral → Clinician (many-to-one): GP creates referral
- Referral → Clinician (many-to-one): Specialist receives referral

## Data Flow

### Appointment Creation Flow
1. User interacts with `AppointmentPanel` (View)
2. View calls `HealthcareController.createAppointment()` (Controller)
3. Controller creates `Appointment` object (Model)
4. Controller saves to CSV via `CSVHandler`
5. View refreshes to display new appointment

### Referral Creation Flow (Singleton Pattern)
1. User fills referral form in `ReferralPanel` (View)
2. View calls `HealthcareController.createReferral()` (Controller)
3. Controller creates `Referral` object (Model)
4. Controller calls `ReferralManager.getInstance().addReferral()` (Singleton)
5. ReferralManager:
   - Adds to queue
   - Generates email communication → `email_communications.txt`
   - Updates EHR → `ehr_updates.txt`
   - Returns control to Controller
6. Controller saves referral via `CSVHandler`
7. View refreshes and confirms success

## Data Persistence Strategy

### CSV-Based Storage
- **Format**: Comma-Separated Values (CSV)
- **Location**: `data/` directory
- **Encoding**: UTF-8

### File Structure
```
data/
├── patients.csv          # Patient records
├── clinicians.csv        # Healthcare professionals
├── appointments.csv      # Appointment bookings
├── prescriptions.csv     # Prescription records
├── referrals.csv         # Referral records
├── email_communications.txt   # Generated by Singleton
└── ehr_updates.txt            # Generated by Singleton
```

### Read/Write Operations
- **Reading**: `CSVHandler.read*()` methods parse CSV into Model objects
- **Writing**: `CSVHandler.write*()` methods serialize Model objects to CSV
- **Appending**: New prescriptions and referrals appended to existing files

### Benefits of CSV Approach
- Human-readable format
- Easy to inspect and debug
- No database setup required
- Simple backup and restore
- Platform-independent

## User Workflows

### Healthcare Professional Workflow
1. Log into system (future authentication)
2. View patient list
3. Access patient appointments
4. Review medical history
5. Create prescription for patient
6. Create referral to specialist if needed
7. Document clinical notes

### Patient Management Workflow (Staff)
1. Register new patient
2. Assign to GP
3. Schedule appointments
4. Update contact information
5. View appointment history
6. View prescriptions and referrals

### Referral Processing Workflow
1. GP identifies need for specialist consultation
2. Creates referral with:
   - Patient details
   - Clinical reason
   - Urgency level (ROUTINE, URGENT, EMERGENCY)
   - Clinical notes
3. Singleton ReferralManager processes:
   - Generates email to specialist
   - Updates patient EHR
   - Logs in audit files
4. Specialist receives notification
5. Specialist updates referral status (ACCEPTED/DECLINED)
6. Appointment scheduled with specialist
7. Referral marked COMPLETED after consultation

## System Features by User Type

### General Practitioners (GPs)
- View assigned patients
- Create appointments
- Create prescriptions based on clinical assessment
- Create referrals to specialists
- View patient medical history

### Specialists
- View incoming referrals
- Accept/decline referrals
- Update referral status
- Conduct specialist appointments
- Create specialist prescriptions

### Nurses
- View appointments
- Assist with patient care
- Document clinical observations
- Support appointment management

### Administrative Staff
- Register new patients
- Schedule and manage appointments
- Update patient contact details
- Handle appointment cancellations
- Manage facility operations

## Technical Specifications

### Programming Language
- **Java 8** (compatible with Java 8+)
- Standard library only (no external dependencies)

### GUI Framework
- **Java Swing** (javax.swing package)
- Standard components: JFrame, JPanel, JTable, JTabbedPane
- Look and Feel: System default

### Data Handling
- **CSV parsing**: Custom implementation in CSVHandler
- **Date/Time**: java.time.LocalDate, LocalDateTime
- **Collections**: ArrayList, LinkedList, Queue

### Design Principles Applied
- **Single Responsibility**: Each class has one clear purpose
- **Open/Closed**: Open for extension, closed for modification
- **Liskov Substitution**: Clinician types are substitutable
- **Interface Segregation**: Clean method interfaces
- **Dependency Inversion**: Depend on abstractions (User base class)

## Future Enhancements

### Phase 2 - Security
- User authentication system
- Role-based access control (RBAC)
- Secure password storage
- Session management
- Audit logging for all actions

### Phase 3 - Database Integration
- Replace CSV with relational database (MySQL/PostgreSQL)
- SQL queries for complex searches
- Transaction support for data integrity
- Concurrent user support
- Better performance for large datasets

### Phase 4 - Advanced Features
- Email integration (SMTP for real emails)
- SMS appointment reminders
- Patient portal for self-service
- Medical history analytics
- Prescription interaction checking
- Billing and insurance claims
- Reporting dashboard

### Phase 5 - Mobile & Web
- RESTful API backend
- Web-based interface
- Mobile app for patients
- Telemedicine support
- Cloud deployment

## Testing Strategy

### Unit Testing (Recommended)
- Test Model classes for data integrity
- Test Controller business logic
- Test CSVHandler parsing
- Test ReferralManager singleton behavior

### Integration Testing
- Test MVC component interaction
- Test data persistence (CSV read/write)
- Test referral workflow end-to-end
- Test appointment lifecycle

### Manual Testing Checklist
- [ ] Load all CSV data successfully
- [ ] View patients in table
- [ ] Create new appointment
- [ ] Modify existing appointment
- [ ] Cancel appointment
- [ ] Create prescription (verify CSV update)
- [ ] Create referral (verify email & EHR generation)
- [ ] Update referral status
- [ ] Verify Singleton pattern (single instance)

## Performance Considerations

### Current Implementation
- In-memory data storage after CSV load
- O(n) search operations for most queries
- Suitable for small to medium datasets (< 10,000 records)

### Optimization Opportunities
- Implement HashMap indexes for faster lookups
- Lazy loading for large datasets
- Pagination for table views
- Caching frequently accessed data
- Background thread for CSV writes

## Error Handling

### Current Approach
- Try-catch blocks for file I/O operations
- User-friendly error dialogs in GUI
- Console logging for debugging
- Validation before data persistence

### Recommended Improvements
- Custom exception hierarchy
- Centralized error logging
- Detailed error reporting
- Recovery mechanisms for data corruption
- Input validation framework

## Code Maintenance Guidelines

### Adding New Entity
1. Create Model class in `src/model/`
2. Add CSV read/write methods in `CSVHandler`
3. Create panel in `src/view/`
4. Add controller methods in `HealthcareController`
5. Add tab to `MainFrame`
6. Create sample CSV data
7. Test thoroughly
8. Update documentation

### Modifying Existing Feature
1. Identify affected components (M, V, or C)
2. Update Model if data structure changes
3. Update View if UI changes needed
4. Update Controller if business logic changes
5. Update CSV format if needed (maintain backward compatibility)
6. Test all affected workflows
7. Commit with descriptive message

### Code Review Checklist
- [ ] Follows Java naming conventions
- [ ] Methods have single responsibility
- [ ] No magic numbers (use constants)
- [ ] Proper error handling
- [ ] Code is commented where complex
- [ ] No code duplication
- [ ] MVC separation maintained
- [ ] Singleton pattern not violated

## Conclusion

This Healthcare Management System demonstrates professional software engineering practices through:

1. **Clean Architecture**: MVC pattern ensures maintainability
2. **Design Patterns**: Singleton ensures referral consistency
3. **Best Practices**: Clear code organization, proper git workflow
4. **Comprehensive Documentation**: README, BUILD, and DESIGN docs
5. **Incremental Development**: Regular git commits showing progress
6. **Real-world Application**: Addresses actual healthcare needs

The system provides a solid foundation for further development and demonstrates understanding of software design principles, architectural patterns, and practical implementation skills.
