/**
 * This is the JUnit test suite for the TP3 application,
 * containing unit tests for various classes.
 * 
 * The tests validate correct behavior and exception handling in the application.
 */
package application.tests;

import application.*; // Ensure correct imports from your application package
import org.junit.jupiter.api.*; // JUnit 5 annotations
import static org.junit.jupiter.api.Assertions.*; // Assertions for testing

/**
 * Test suite for validating the functionality of the Answer, Message, Question,
 * Request, Review, and TrustedReviewer classes.
 */
public class TP3TestSuite {

    /**
     * Tests that setAuthor correctly assigns a valid author name.
     */
    @Test
    public void testSetAuthor_validInput_setsAuthor() {
        Answer a = new Answer();
        a.setAuthor("Alice");
        assertEquals("Alice", a.getAuthor()); // Verify the author was set correctly
    }

    /**
     * Tests that setting a null author throws an IllegalArgumentException.
     */
    @Test
    public void testSetAuthor_null_throwsException() {
        Answer a = new Answer();
        assertThrows(IllegalArgumentException.class, () -> a.setAuthor(null)); // Should throw on null input
    }

    /**
     * Tests setting a valid answer text.
     */
    @Test
    public void testSetAnswerText_valid_setsText() {
        Answer a = new Answer();
        a.setAnswerText("This is a good answer.");
        assertEquals("This is a good answer.", a.getAnswerText()); // Confirm text was set
    }

    /**
     * Tests that setting an empty answer text throws an exception.
     */
    @Test
    public void testSetAnswerText_blank_throwsException() {
        Answer a = new Answer();
        assertThrows(IllegalArgumentException.class, () -> a.setAnswerText("")); // Blank input should fail
    }

    /**
     * Tests that upvoting an answer increments the upvote count.
     */
    @Test
    public void testUpvote_incrementsUpvotes() {
        Answer a = new Answer();
        int original = a.getUpvotes(); // Capture initial upvotes
        a.upvote();
        assertEquals(original + 1, a.getUpvotes()); // Verify increment
    }

    /**
     * Tests that downvoting an answer increments the downvote count.
     */
    @Test
    public void testDownvote_incrementsDownvotes() {
        Answer a = new Answer();
        int original = a.getDownvotes(); // Capture initial downvotes
        a.downvote();
        assertEquals(original + 1, a.getDownvotes()); // Verify increment
    }

    /**
     * Tests that the toString method includes 'Accepted' when an answer is accepted.
     */
    @Test
    public void testToString_includesAccepted_whenTrue() {
        Answer a = new Answer();
        a.setAnswerText("Test Answer");
        a.setAccepted(true); // Mark as accepted
        assertTrue(a.toString().contains("Accepted")); // Should appear in string representation
    }

    /**
     * Tests the set and get methods for the fromUser field in the Message class.
     */
    @Test
    public void testSetAndGetFromUser_returnsCorrectValue() {
        Message m = new Message();
        m.setFromUser("alice");
        assertEquals("alice", m.getFromUser()); // Confirm user set correctly
    }

    /**
     * Tests setting and retrieving the content of a message.
     */
    @Test
    public void testSetAndGetContent_returnsCorrectContent() {
        Message m = new Message();
        m.setContent("Hey there");
        assertEquals("Hey there", m.getContent()); // Check if content matches
    }

    /**
     * Tests setting and retrieving the read flag of a message.
     */
    @Test
    public void testSetRead_setsReadFlagCorrectly() {
        Message m = new Message();
        m.setRead(true);
        assertTrue(m.isRead()); // Should reflect updated read status
    }

    /**
     * Tests setting and getting the question title.
     */
    @Test
    public void testSetAndGetQuestionTitle() {
        Question q = new Question();
        q.setQuestionTitle("JUnit Testing");
        assertEquals("JUnit Testing", q.getQuestionTitle()); // Title should be as set
    }

    /**
     * Tests setting and getting the question text.
     */
    @Test
    public void testSetAndGetQuestionText() {
        Question q = new Question();
        q.setQuestionText("How to write unit tests?");
        assertEquals("How to write unit tests?", q.getQuestionText()); // Text should match
    }

    /**
     * Tests setting and getting the status of a request.
     */
    @Test
    public void testSetStatus() {
        Request r = new Request();
        r.setStatus("CLOSED");
        assertEquals("CLOSED", r.getStatus()); // Status should be updated
    }

    /**
     * Tests that reopening a request maintains the original request ID.
     */
    @Test
    public void testReopenRequestKeepsOriginalId() {
        Request r = new Request();
        r.setOriginalRequestId(101);
        assertEquals(101, r.getOriginalRequestId()); // ID should remain consistent
    }

    /**
     * Tests setting and getting a review rating.
     */
    @Test
    public void testSetRating() {
        Review review = new Review();
        review.setRating(4);
        assertEquals(4, review.getRating()); // Rating should be stored and retrieved
    }

    /**
     * Tests that a new review object has a non-null creation time.
     */
    @Test
    public void testDefaultCreationTimeIsNow() {
        Review review = new Review();
        assertNotNull(review.getCreationTime()); // Should auto-generate creation time
    }

    /**
     * Tests setting and retrieving the weight of a TrustedReviewer.
     */
    @Test
    public void testTrustedReviewerWeightSetAndGet() {
        TrustedReviewer tr = new TrustedReviewer();
        tr.setWeight(7);
        assertEquals(7, tr.getWeight()); // Weight should be as set
    }
}