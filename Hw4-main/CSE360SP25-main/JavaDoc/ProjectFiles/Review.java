// File: Review.java

package application;

import java.time.LocalDateTime;

/**
 * Represents a review given by a trusted reviewer for a specific answer.
 * Each review includes metadata such as the reviewer's username, review content,
 * an optional rating, and the timestamp when it was created.
 * 
 * This class provides getter and setter methods for accessing and modifying the review's attributes.
 */
public class Review {

    /** Unique identifier for the review */
    private int reviewId;

    /** The ID of the answer being reviewed */
    private int answerId;

    /** The username of the reviewer who wrote the review */
    private String reviewerUserName;

    /** The text content of the review */
    private String reviewText;

    /** An optional numeric rating (e.g., 1 to 5 stars) */
    private int rating;

    /** The timestamp when the review was created */
    private LocalDateTime creationTime;

    /**
     * Constructs a new Review with the current timestamp.
     */
    public Review() {
        creationTime = LocalDateTime.now();
    }

    // -------------------- Getters and Setters --------------------

    /**
     * Gets the unique ID of the review.
     * 
     * @return the review ID
     */
    public int getReviewId() {
        return reviewId;
    }

    /**
     * Sets the unique ID of the review.
     * 
     * @param reviewId the review ID to set
     */
    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    /**
     * Gets the ID of the answer this review refers to.
     * 
     * @return the answer ID
     */
    public int getAnswerId() {
        return answerId;
    }

    /**
     * Sets the ID of the answer this review refers to.
     * 
     * @param answerId the answer ID to set
     */
    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    /**
     * Gets the username of the reviewer.
     * 
     * @return the reviewer's username
     */
    public String getReviewerUserName() {
        return reviewerUserName;
    }

    /**
     * Sets the username of the reviewer.
     * 
     * @param reviewerUserName the username to set
     */
    public void setReviewerUserName(String reviewerUserName) {
        this.reviewerUserName = reviewerUserName;
    }

    /**
     * Gets the text content of the review.
     * 
     * @return the review text
     */
    public String getReviewText() {
        return reviewText;
    }

    /**
     * Sets the text content of the review.
     * 
     * @param reviewText the review text to set
     */
    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    /**
     * Gets the rating associated with the review.
     * 
     * @return the numeric rating
     */
    public int getRating() {
        return rating;
    }

    /**
     * Sets the numeric rating for the review.
     * 
     * @param rating the rating to set
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * Gets the timestamp when the review was created.
     * 
     * @return the creation time
     */
    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    /**
     * Sets the creation time of the review.
     * 
     * @param creationTime the timestamp to set
     */
    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }
}