package application;

public class TrustedReviewer {
    private String ownerUserName;    // The student who trusts
    private String reviewerUserName; // The reviewer being trusted
    private int weight;              // Trust weight or rating

    public String getOwnerUserName() { return ownerUserName; }
    public void setOwnerUserName(String ownerUserName) { this.ownerUserName = ownerUserName; }

    public String getReviewerUserName() { return reviewerUserName; }
    public void setReviewerUserName(String reviewerUserName) { this.reviewerUserName = reviewerUserName; }

    public int getWeight() { return weight; }
    public void setWeight(int weight) { this.weight = weight; }
}
