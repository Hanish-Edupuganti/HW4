// File: TrustedReviewer.java

/**
 * <p><b>Title:</b> TrustedReviewer</p>
 * 
 * <p><b>Description:</b> This class represents a trust relationship where a student 
 * trusts a reviewer. It stores the reviewer's username, the owner's (student's) username, 
 * and an associated trust weight or rating indicating the level of trust.</p>
 * 
 * <p>Used in systems where peer review credibility or trust levels influence decisions, 
 * rankings, or visibility of reviews.</p>
 * 
 * <p><b>Example:</b>
 * A student "alice" trusts reviewer "bob" with a weight of 3.</p>
 * 
 * <p><b>Author:</b> Lynn Robert Carter</p>
 * 
 * @version 1.00 — 2025-04-01 Initial implementation
 */
public class TrustedReviewer {
    
    /** The username of the student who trusts the reviewer */
    private String ownerUserName;
    
    /** The username of the reviewer who is being trusted */
    private String reviewerUserName;
    
    /** The numeric weight or rating that indicates trust level (e.g., 1 to 5) */
    private int weight;

    /**
     * Gets the username of the student (trust owner).
     * 
     * @return the owner's username
     */
    public String getOwnerUserName() {
        return ownerUserName;
    }

    /**
     * Sets the username of the student (trust owner).
     * 
     * @param ownerUserName the username of the student assigning trust
     */
    public void setOwnerUserName(String ownerUserName) {
        this.ownerUserName = ownerUserName;
    }

    /**
     * Gets the username of the reviewer who is being trusted.
     * 
     * @return the reviewer's username
     */
    public String getReviewerUserName() {
        return reviewerUserName;
    }

    /**
     * Sets the username of the reviewer who is being trusted.
     * 
     * @param reviewerUserName the username of the reviewer being trusted
     */
    public void setReviewerUserName(String reviewerUserName) {
        this.reviewerUserName = reviewerUserName;
    }

    /**
     * Gets the trust weight or rating.
     * 
     * @return the weight value indicating level of trust
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Sets the trust weight or rating.
     * 
     * @param weight a numeric value representing trust level
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }
}