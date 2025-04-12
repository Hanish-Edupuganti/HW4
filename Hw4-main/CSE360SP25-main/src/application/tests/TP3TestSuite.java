package application.tests;

import application.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class TP3TestSuite {

    // Answer.java tests
    @Test
    public void testSetAuthor_validInput_setsAuthor() {
        Answer a = new Answer();
        a.setAuthor("Alice");
        assertEquals("Alice", a.getAuthor());
    }

    @Test
    public void testSetAuthor_null_throwsException() {
        Answer a = new Answer();
        assertThrows(IllegalArgumentException.class, () -> a.setAuthor(null));
    }

    @Test
    public void testSetAnswerText_valid_setsText() {
        Answer a = new Answer();
        a.setAnswerText("This is a good answer.");
        assertEquals("This is a good answer.", a.getAnswerText());
    }

    @Test
    public void testSetAnswerText_blank_throwsException() {
        Answer a = new Answer();
        assertThrows(IllegalArgumentException.class, () -> a.setAnswerText(""));
    }

    @Test
    public void testUpvote_incrementsUpvotes() {
        Answer a = new Answer();
        int original = a.getUpvotes();
        a.upvote();
        assertEquals(original + 1, a.getUpvotes());
    }

    @Test
    public void testDownvote_incrementsDownvotes() {
        Answer a = new Answer();
        int original = a.getDownvotes();
        a.downvote();
        assertEquals(original + 1, a.getDownvotes());
    }

    @Test
    public void testToString_includesAccepted_whenTrue() {
        Answer a = new Answer();
        a.setAnswerText("Test Answer");
        a.setAccepted(true);
        assertTrue(a.toString().contains("Accepted"));
    }

    // Message.java tests
    @Test
    public void testSetAndGetFromUser_returnsCorrectValue() {
        Message m = new Message();
        m.setFromUser("alice");
        assertEquals("alice", m.getFromUser());
    }

    @Test
    public void testSetAndGetContent_returnsCorrectContent() {
        Message m = new Message();
        m.setContent("Hey there");
        assertEquals("Hey there", m.getContent());
    }

    @Test
    public void testSetRead_setsReadFlagCorrectly() {
        Message m = new Message();
        m.setRead(true);
        assertTrue(m.isRead());
    }

    // Question.java
    @Test
    public void testSetAndGetQuestionTitle() {
        Question q = new Question();
        q.setQuestionTitle("JUnit Testing");
        assertEquals("JUnit Testing", q.getQuestionTitle());
    }

    @Test
    public void testSetAndGetQuestionText() {
        Question q = new Question();
        q.setQuestionText("How to write unit tests?");
        assertEquals("How to write unit tests?", q.getQuestionText());
    }

    // Request.java
    @Test
    public void testSetStatus() {
        Request r = new Request();
        r.setStatus("CLOSED");
        assertEquals("CLOSED", r.getStatus());
    }

    @Test
    public void testReopenRequestKeepsOriginalId() {
        Request r = new Request();
        r.setOriginalRequestId(101);
        assertEquals(101, r.getOriginalRequestId());
    }

    // Review.java
    @Test
    public void testSetRating() {
        Review review = new Review();
        review.setRating(4);
        assertEquals(4, review.getRating());
    }

    @Test
    public void testDefaultCreationTimeIsNow() {
        Review review = new Review();
        assertNotNull(review.getCreationTime());
    }
    
    @Test
    public void testTrustedReviewerWeightSetAndGet() {
        TrustedReviewer tr = new TrustedReviewer();
        tr.setWeight(7);
        assertEquals(7, tr.getWeight());
    }

    @Test
    public void testSetSingleRoleAndGetRolesList() {
        User u = new User("testUser", "pass", "student");
        u.setRole("admin");
        assertTrue(u.getRoles().contains("admin"));
    }

    @Test
    public void testPasswordEvaluatorValidPassword() {
        String result = PasswordEvaluator.evaluatePassword("Valid123!");
        assertEquals("", result); 
    }

    @Test
    public void testStartCSE360Singletons() {
        assertNotNull(StartCSE360.getDatabaseHelper());
    }

    @Test
    public void testTrustedReviewerSetGetReviewerUserName() {
        TrustedReviewer tr = new TrustedReviewer();
        tr.setReviewerUserName("reviewer123");
        assertEquals("reviewer123", tr.getReviewerUserName());
    }

    @Test
    public void testUserObjectRoleAssignment() {
        User u = new User("bob", "securePass", "student");
        u.setRole("reviewer");
        assertEquals("reviewer", u.getRole());
    }
    
 // UserNameRecognizer.java test
    @Test
    public void testValidUsername() {
        String result = UserNameRecognizer.checkForValidUserName("validUser123");
        assertEquals("", result);
    }

    @Test
    public void testInvalidUsernameTooShort() {
        String result = UserNameRecognizer.checkForValidUserName("a");
        assertTrue(result.contains("at least 4 characters"));
    }

    @Test
    public void testInvalidUsernameStartsWithDigit() {
        String result = UserNameRecognizer.checkForValidUserName("1badName");
        assertTrue(result.contains("must start with A-Z or a-z"));
    }

    @Test
    public void testUserNameRetrieval() {
        User u = new User("maxwell", "pw", "staff");
        assertEquals("maxwell", u.getUserName());
    }

    // Review.java test
    @Test
    public void testSetAndGetReviewerName() {
        Review r = new Review();
        r.setReviewerUserName("bharani");
        assertEquals("bharani", r.getReviewerUserName());
    }

    // RoleSelectionPage routing logic mock (conceptual test)
    @Test
    public void testRoleSelectionRedirect() {
        User mockUser = new User("hanish", "pw", "student");
        mockUser.setRole("instructor");
        assertTrue(mockUser.getRoles().contains("instructor"));
    }

    // TrustedReviewer weight test
    @Test
    public void testWeightageLimit() {
        TrustedReviewer tr = new TrustedReviewer();
        tr.setWeight(10);
        assertTrue(tr.getWeight() <= 10);
    }
    
    @Test
    public void testSetAndGetQuestionAuthor() {
        Question q = new Question();
        q.setAuthor("sunyen");
        assertEquals("sunyen", q.getAuthor());
    }
}
