package model;

/**
 * AdminStaff entity representing administrative/non-clinical staff (receptionist, etc.)
 */
public class AdminStaff extends User {
    private String staffId;
    private String role; // RECEPTIONIST, ADMIN
    private String department;

    public AdminStaff(String staffId, String firstName, String lastName, String email, 
                      String phone, String role, String department) {
        super(staffId, firstName, lastName, email, phone, "ADMIN_STAFF");
        this.staffId = staffId;
        this.role = role;
        this.department = department;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return String.format("%s: %s (%s)", role, getFullName(), department);
    }
}
