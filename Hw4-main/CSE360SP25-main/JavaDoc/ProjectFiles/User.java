// File: User.java

/**
 * The {@code User} class represents a user in the system.
 * <p>
 * Each user has a username, password, and a list of roles. Roles are stored internally
 * as a comma-separated string, which can be accessed either as a raw string or a parsed list.
 * </p>
 * <p>
 * This class provides convenience methods to get and set the user's identity and roles.
 * </p>
 */
public class User {

    /** The username of the user (used for login and identification) */
    private String userName;

    /** The password of the user (stored as plain text for now; consider hashing in production) */
    private String password;

    /**
     * The role(s) of the user, stored as a comma-separated string (e.g., "admin,student").
     * This format allows support for multi-role users.
     */
    private String role;

    /**
     * Constructs a new {@code User} with the specified username, password, and role(s).
     *
     * @param userName The username to identify the user.
     * @param password The user's password.
     * @param role     A comma-separated string of roles assigned to the user.
     */
    public User(String userName, String password, String role) {
        this.userName = userName;
        this.password = password;
        this.role = role;
    }

    /**
     * Returns the username of the user.
     *
     * @return The user's username.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Returns the user's password.
     *
     * @return The password assigned to the user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Retrieves the user's roles as a {@code List<String>}.
     * Splits the comma-separated role string into individual roles.
     *
     * @return A list of role names. Returns an empty list if no roles are assigned.
     */
    public List<String> getRoles() {
        if (role == null || role.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(role.split(","));
    }

    /**
     * Retrieves the user's role(s) as a single comma-separated string.
     *
     * @return The comma-separated string representing all assigned roles.
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the user's roles using a list of strings.
     * Converts the list to a single comma-separated role string.
     *
     * @param roles A list of roles to assign to the user.
     */
    public void setRoles(List<String> roles) {
        this.role = String.join(",", roles);
    }

    /**
     * Sets the user's roles as a raw comma-separated string.
     *
     * @param role A string containing one or more roles, separated by commas.
     */
    public void setRole(String role) {
        this.role = role;
    }
}