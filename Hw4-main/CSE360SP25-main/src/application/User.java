package application;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class User {
    private String userName;
    private String password;
    // Roles stored as a comma-separated string (e.g., "admin,student")
    private String role; 

    public User(String userName, String password, String role) {
        this.userName = userName;
        this.password = password;
        this.role = role;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public String getPassword() {
        return password;
    }
    
    // Returns roles as a list.
    public List<String> getRoles() {
        if (role == null || role.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(role.split(","));
    }
    
    // Returns the entire role string.
    public String getRole() { 
        return role; 
    }
    
    // Set roles from a list.
    public void setRoles(List<String> roles) {
        this.role = String.join(",", roles);
    }
    
    // Convenience setter for a single role.
    public void setRole(String role) {
        this.role = role;
    }
}
