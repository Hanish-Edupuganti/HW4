// File: Request.java

/**
 * Represents a request entity used in the application.
 * A request can be created by a user, assigned to someone (like admin/staff),
 * and can include status tracking, notes, and support for reopening requests.
 */
public class Request {

    /** Unique ID for this request */
    private int requestId;

    /** Username of the person who created the request */
    private String createdBy;

    /** Username of the person the request is assigned to */
    private String assignedTo;

    /** Description or details about the request */
    private String description;

    /** Current status of the request (e.g., OPEN, CLOSED, REOPENED) */
    private String status;

    /** Notes related to this request (used by reviewers/admins for updates) */
    private String notes;

    /** If this request is a reopened one, it holds the ID of the original request */
    private Integer originalRequestId;

    // --- Getters and Setters ---

    /**
     * Returns the request ID.
     * @return request ID
     */
    public int getRequestId() {
        return requestId;
    }

    /**
     * Sets the request ID.
     * @param requestId the unique ID to assign to the request
     */
    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    /**
     * Returns the creator of the request.
     * @return username of the creator
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the creator of the request.
     * @param createdBy username of the creator
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Returns the assignee of the request.
     * @return username of the person assigned to this request
     */
    public String getAssignedTo() {
        return assignedTo;
    }

    /**
     * Sets the assignee of the request.
     * @param assignedTo username of the person to assign the request to
     */
    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    /**
     * Returns the description of the request.
     * @return description text
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description for the request.
     * @param description the details of the request
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the current status of the request.
     * @return status (e.g., OPEN, CLOSED)
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the current status of the request.
     * @param status new status value
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Returns any notes attached to the request.
     * @return additional notes or remarks
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets notes for the request.
     * @param notes remarks or update notes
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Gets the ID of the original request if this is a reopened one.
     * @return original request ID or null if not reopened
     */
    public Integer getOriginalRequestId() {
        return originalRequestId;
    }

    /**
     * Sets the original request ID when this is a reopened request.
     * @param originalRequestId ID of the original request
     */
    public void setOriginalRequestId(Integer originalRequestId) {
        this.originalRequestId = originalRequestId;
    }
}