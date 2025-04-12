package application;

public class Request {
    private int requestId;
    private String createdBy;
    private String assignedTo;
    private String description;
    private String status; // e.g., OPEN, CLOSED, REOPENED
    private String notes;
    private Integer originalRequestId; // Link to original request if reopened

    public int getRequestId() {
        return requestId;
    }
    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getAssignedTo() {
        return assignedTo;
    }
    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getOriginalRequestId() {
        return originalRequestId;
    }
    public void setOriginalRequestId(Integer originalRequestId) {
        this.originalRequestId = originalRequestId;
    }
}
