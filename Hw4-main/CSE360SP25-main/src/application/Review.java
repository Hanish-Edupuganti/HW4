package application;

import java.time.LocalDateTime;

public class Review {
    private int reviewId;
    private int answerId;
    private String reviewerUserName;
    private String reviewText;
    private int rating; // Optional rating (e.g., 1-5)
    private LocalDateTime creationTime;

    public Review() {
        creationTime = LocalDateTime.now();
    }

    // Getters and setters
    public int getReviewId() { return reviewId; }
    public void setReviewId(int reviewId) { this.reviewId = reviewId; }

    public int getAnswerId() { return answerId; }
    public void setAnswerId(int answerId) { this.answerId = answerId; }

    public String getReviewerUserName() { return reviewerUserName; }
    public void setReviewerUserName(String reviewerUserName) { this.reviewerUserName = reviewerUserName; }

    public String getReviewText() { return reviewText; }
    public void setReviewText(String reviewText) { this.reviewText = reviewText; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public LocalDateTime getCreationTime() { return creationTime; }
    public void setCreationTime(LocalDateTime creationTime) { this.creationTime = creationTime; }
}
