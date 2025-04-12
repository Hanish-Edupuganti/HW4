package databasePart1;

import application.Answer;
import application.Question;
import application.User;
import application.Message;
import application.Review;
import application.TrustedReviewer;
import application.Request;  // Assuming you have a Request.java in your application package

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseHelper {

    // JDBC driver name and database URL 
    static final String JDBC_DRIVER = "org.h2.Driver";   
    static final String DB_URL = "jdbc:h2:~/FoundationDatabase";  

    // Database credentials 
    static final String USER = "sa"; 
    static final String PASS = ""; 

    private Connection connection = null;
    private Statement statement = null; 

    // --------------------- Connection and Table Creation ---------------------

    public void connectToDatabase() throws SQLException {
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("Connecting to database...");
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            statement = connection.createStatement();
            createTables();
            createQATables();
            createAdditionalTables(); // Creates messages, reviews, trusted reviewers, scorecard parameters, and requests tables
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found: " + e.getMessage());
        }
    }

    // Create user and invitation tables.
    private void createTables() throws SQLException {
        String userTable = "CREATE TABLE IF NOT EXISTS cse360users ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "userName VARCHAR(255) UNIQUE, "
                + "password VARCHAR(255), "
                + "role VARCHAR(255))";  // Multiple roles stored as comma-separated.
        statement.execute(userTable);

        String invitationCodesTable = "CREATE TABLE IF NOT EXISTS InvitationCodes ("
                + "code VARCHAR(10) PRIMARY KEY, "
                + "isUsed BOOLEAN DEFAULT FALSE)";
        statement.execute(invitationCodesTable);
    }

    // Create questions and answers tables.
    private void createQATables() throws SQLException {
        String createQuestions = "CREATE TABLE IF NOT EXISTS cse360questions ("
                + "question_id INT AUTO_INCREMENT PRIMARY KEY, "
                + "author VARCHAR(255) NOT NULL, "
                + "question_title VARCHAR(255) NOT NULL, "
                + "question_text TEXT NOT NULL, "
                + "creation_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                + "solved BOOLEAN DEFAULT FALSE, "
                + "accepted_answer_id INT DEFAULT 0"
                + ")";
        statement.execute(createQuestions);

        String createAnswers = "CREATE TABLE IF NOT EXISTS cse360answers ("
                + "answer_id INT AUTO_INCREMENT PRIMARY KEY, "
                + "question_id INT NOT NULL, "
                + "author VARCHAR(255) NOT NULL, "
                + "answer_text TEXT NOT NULL, "
                + "creation_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                + "upvotes INT DEFAULT 0, "
                + "downvotes INT DEFAULT 0, "
                + "accepted BOOLEAN DEFAULT FALSE"
                + ")";
        statement.execute(createAnswers);
    }

    // Create additional tables: messages, reviews, trusted reviewers, scorecard parameters, and requests.
    private void createAdditionalTables() throws SQLException {
        // Messages table
        String createMessages = "CREATE TABLE IF NOT EXISTS cse360messages ("
                + "message_id INT AUTO_INCREMENT PRIMARY KEY, "
                + "from_user VARCHAR(255) NOT NULL, "
                + "to_user VARCHAR(255) NOT NULL, "
                + "question_id INT, "
                + "content TEXT NOT NULL, "
                + "creation_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                + "is_read BOOLEAN DEFAULT FALSE"
                + ")";
        statement.execute(createMessages);

        // Reviews table
        String createReviews = "CREATE TABLE IF NOT EXISTS cse360reviews ("
                + "review_id INT AUTO_INCREMENT PRIMARY KEY, "
                + "answer_id INT NOT NULL, "
                + "reviewer_user VARCHAR(255) NOT NULL, "
                + "review_text TEXT NOT NULL, "
                + "rating INT DEFAULT 0, "
                + "creation_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                + ")";
        statement.execute(createReviews);

        // Trusted reviewers table
        String createTrustedReviewers = "CREATE TABLE IF NOT EXISTS cse360trusted_reviewers ("
                + "owner_user VARCHAR(255) NOT NULL, "
                + "reviewer_user VARCHAR(255) NOT NULL, "
                + "weight INT DEFAULT 5, "
                + "PRIMARY KEY(owner_user, reviewer_user)"
                + ")";
        statement.execute(createTrustedReviewers);

        // Scorecard parameters table
        String createScorecardParams = "CREATE TABLE IF NOT EXISTS cse360scorecard_params ("
                + "param_name VARCHAR(100) PRIMARY KEY, "
                + "param_value INT"
                + ")";
        statement.execute(createScorecardParams);

        // Requests table
        String createRequests = "CREATE TABLE IF NOT EXISTS cse360requests ("
                + "request_id INT AUTO_INCREMENT PRIMARY KEY, "
                + "created_by VARCHAR(255) NOT NULL, "
                + "assigned_to VARCHAR(255), "
                + "description TEXT NOT NULL, "
                + "status VARCHAR(50) NOT NULL DEFAULT 'OPEN', "
                + "original_request_id INT, "
                + "notes TEXT"
                + ")";
        statement.execute(createRequests);
    }

    // --------------------- User Methods ---------------------

    public boolean isDatabaseEmpty() throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM cse360users";
        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.next()) {
            return resultSet.getInt("count") == 0;
        }
        return true;
    }

    public void register(User user) throws SQLException {
        String insertUser = "INSERT INTO cse360users (userName, password, role) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
            pstmt.setString(1, user.getUserName());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getRole());
            pstmt.executeUpdate();
        }
    }

    public boolean login(User user) throws SQLException {
        String query = "SELECT * FROM cse360users WHERE userName = ? AND password = ? AND role = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, user.getUserName());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getRole());
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean doesUserExist(String userName) {
        String query = "SELECT COUNT(*) FROM cse360users WHERE userName = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, userName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getUserRole(String userName) {
        String query = "SELECT role FROM cse360users WHERE userName = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, userName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("role");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String generateInvitationCode() {
        String code = UUID.randomUUID().toString().substring(0, 4);
        String query = "INSERT INTO InvitationCodes (code) VALUES (?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, code);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return code;
    }

    public boolean validateInvitationCode(String code) {
        String query = "SELECT * FROM InvitationCodes WHERE code = ? AND isUsed = FALSE";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, code);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                markInvitationCodeAsUsed(code);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void markInvitationCodeAsUsed(String code) {
        String query = "UPDATE InvitationCodes SET isUsed = TRUE WHERE code = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, code);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (statement != null) statement.close();
        } catch (SQLException se2) {
            se2.printStackTrace();
        }
        try {
            if (connection != null) connection.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM cse360users";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String username = rs.getString("userName");
                String password = rs.getString("password");
                String role = rs.getString("role");
                users.add(new User(username, password, role));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void deleteUser(String userName) {
        String sql = "DELETE FROM cse360users WHERE userName = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, userName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean resetPassword(String userName, String newPassword) {
        String sql = "UPDATE cse360users SET password = ? WHERE userName = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, userName);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // NEW: Set a single role for a user (overwrites any existing roles)
    public void setSingleRoleForUser(String userName, String newRole) {
        String sql = "UPDATE cse360users SET role=? WHERE userName=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newRole);
            pstmt.setString(2, userName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---------------- Questions & Answers ----------------

    public void createQuestion(Question q) throws SQLException {
        String sql = "INSERT INTO cse360questions (author, question_title, question_text, creation_time, solved, accepted_answer_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, q.getAuthor());
            pstmt.setString(2, q.getQuestionTitle());
            pstmt.setString(3, q.getQuestionText());
            pstmt.setTimestamp(4, Timestamp.valueOf(q.getCreationTime()));
            pstmt.setBoolean(5, q.isSolved());
            pstmt.setInt(6, q.getAcceptedAnswerID());
            pstmt.executeUpdate();
            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    q.setQuestionID(keys.getInt(1));
                }
            }
        }
        System.out.println("Create question successful");
    }

    public Question getQuestionByID(int questionID) {
        String sql = "SELECT * FROM cse360questions WHERE question_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, questionID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapQuestion(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Question> getAllQuestions() {
        List<Question> result = new ArrayList<>();
        String sql = "SELECT * FROM cse360questions";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result.add(mapQuestion(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Getting questions");
        return result;
    }

    public void updateQuestion(Question q) throws SQLException {
        String sql = "UPDATE cse360questions SET author=?, question_title=?, question_text=?, creation_time=?, solved=?, accepted_answer_id=? WHERE question_id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, q.getAuthor());
            pstmt.setString(2, q.getQuestionTitle());
            pstmt.setString(3, q.getQuestionText());
            pstmt.setTimestamp(4, Timestamp.valueOf(q.getCreationTime()));
            pstmt.setBoolean(5, q.isSolved());
            pstmt.setInt(6, q.getAcceptedAnswerID());
            pstmt.setInt(7, q.getQuestionID());
            pstmt.executeUpdate();
        }
        System.out.println("Updated question successfully");
    }

    public void deleteQuestion(int questionID) throws SQLException {
        String deleteAnswers = "DELETE FROM cse360answers WHERE question_id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteAnswers)) {
            pstmt.setInt(1, questionID);
            pstmt.executeUpdate();
        }
        String sql = "DELETE FROM cse360questions WHERE question_id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, questionID);
            pstmt.executeUpdate();
            System.out.println("Deleted question successfully");
        }
    }

    private Question mapQuestion(ResultSet rs) throws SQLException {
        Question q = new Question();
        q.setQuestionID(rs.getInt("question_id"));
        q.setAuthor(rs.getString("author"));
        q.setQuestionTitle(rs.getString("question_title"));
        q.setQuestionText(rs.getString("question_text"));
        Timestamp ts = rs.getTimestamp("creation_time");
        if (ts != null) {
            LocalDateTime ldt = ts.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            q.setCreationTime(ldt);
        }
        q.setSolved(rs.getBoolean("solved"));
        q.setAcceptedAnswerID(rs.getInt("accepted_answer_id"));
        return q;
    }

    public void createAnswer(Answer a) throws SQLException {
        String sql = "INSERT INTO cse360answers (question_id, author, answer_text, creation_time, upvotes, downvotes, accepted) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, a.getQuestionID());
            pstmt.setString(2, a.getAuthor());
            pstmt.setString(3, a.getAnswerText());
            pstmt.setTimestamp(4, Timestamp.valueOf(a.getCreationTime()));
            pstmt.setInt(5, a.getUpvotes());
            pstmt.setInt(6, a.getDownvotes());
            pstmt.setBoolean(7, a.isAccepted());
            pstmt.executeUpdate();
            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    a.setAnswerID(keys.getInt(1));
                }
            }
        }
        System.out.println("Created Answer successful");
    }

    public Answer getAnswerByID(int answerID) {
        String sql = "SELECT * FROM cse360answers WHERE answer_id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, answerID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapAnswer(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Answer> getAnswersByQuestionID(int questionID) {
        List<Answer> result = new ArrayList<>();
        String sql = "SELECT * FROM cse360answers WHERE question_id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, questionID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result.add(mapAnswer(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Answer> getAllAnswers() {
        List<Answer> result = new ArrayList<>();
        String sql = "SELECT * FROM cse360answers";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result.add(mapAnswer(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Getting all answers");
        return result;
    }

    public void updateAnswer(Answer a) throws SQLException {
        String sql = "UPDATE cse360answers SET question_id=?, author=?, answer_text=?, creation_time=?, upvotes=?, downvotes=?, accepted=? WHERE answer_id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, a.getQuestionID());
            pstmt.setString(2, a.getAuthor());
            pstmt.setString(3, a.getAnswerText());
            pstmt.setTimestamp(4, Timestamp.valueOf(a.getCreationTime()));
            pstmt.setInt(5, a.getUpvotes());
            pstmt.setInt(6, a.getDownvotes());
            pstmt.setBoolean(7, a.isAccepted());
            pstmt.setInt(8, a.getAnswerID());
            pstmt.executeUpdate();
            System.out.println("Updated Answer successfully");
        }
    }

    public void deleteAnswer(int answerID) throws SQLException {
        String sql = "DELETE FROM cse360answers WHERE answer_id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, answerID);
            pstmt.executeUpdate();
            System.out.println("Deleted Answer successfully");
        }
    }

    private Answer mapAnswer(ResultSet rs) throws SQLException {
        Answer a = new Answer();
        a.setAnswerID(rs.getInt("answer_id"));
        a.setQuestionID(rs.getInt("question_id"));
        a.setAuthor(rs.getString("author"));
        a.setAnswerText(rs.getString("answer_text"));
        Timestamp ts = rs.getTimestamp("creation_time");
        if (ts != null) {
            LocalDateTime ldt = ts.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            a.setCreationTime(ldt);
        }
        a.setAccepted(rs.getBoolean("accepted"));
        a.setUpvotesCount(rs.getInt("upvotes"));
        a.setDownvotesCount(rs.getInt("downvotes"));
        return a;
    }

    // ---------------- MESSAGES ----------------

    public void addMessage(Message msg) {
        String sql = "INSERT INTO cse360messages (from_user, to_user, question_id, content, creation_time, is_read) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, msg.getFromUser());
            pstmt.setString(2, msg.getToUser());
            pstmt.setInt(3, msg.getQuestionId());
            pstmt.setString(4, msg.getContent());
            pstmt.setTimestamp(5, Timestamp.valueOf(msg.getCreationTime()));
            pstmt.setBoolean(6, msg.isRead());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Message> getMessagesForUser(String username) {
        List<Message> list = new ArrayList<>();
        String sql = "SELECT * FROM cse360messages WHERE to_user = ? OR from_user = ? ORDER BY creation_time DESC";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, username);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapMessage(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Message> getMessagesForQuestion(int questionId, String currentUserName) {
        List<Message> list = new ArrayList<>();
        String sql = "SELECT * FROM cse360messages WHERE question_id = ? AND (to_user = ? OR from_user = ?) ORDER BY creation_time ASC";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, questionId);
            pstmt.setString(2, currentUserName);
            pstmt.setString(3, currentUserName);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapMessage(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // NEW: Delete a message by ID (affects both sender and recipient)
    public boolean deleteMessage(int messageId) {
        String sql = "DELETE FROM cse360messages WHERE message_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, messageId);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Message mapMessage(ResultSet rs) throws SQLException {
        Message m = new Message();
        m.setMessageId(rs.getInt("message_id"));
        m.setFromUser(rs.getString("from_user"));
        m.setToUser(rs.getString("to_user"));
        m.setQuestionId(rs.getInt("question_id"));
        m.setContent(rs.getString("content"));
        Timestamp ts = rs.getTimestamp("creation_time");
        if (ts != null) {
            m.setCreationTime(ts.toLocalDateTime());
        }
        m.setRead(rs.getBoolean("is_read"));
        return m;
    }

    // ---------------- REVIEWS ----------------

    public void addReview(Review r) {
        String sql = "INSERT INTO cse360reviews (answer_id, reviewer_user, review_text, rating, creation_time) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, r.getAnswerId());
            pstmt.setString(2, r.getReviewerUserName());
            pstmt.setString(3, r.getReviewText());
            pstmt.setInt(4, r.getRating());
            pstmt.setTimestamp(5, Timestamp.valueOf(r.getCreationTime()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Review> getReviewsByAnswerId(int answerId) {
        List<Review> list = new ArrayList<>();
        String sql = "SELECT * FROM cse360reviews WHERE answer_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, answerId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapReview(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Review mapReview(ResultSet rs) throws SQLException {
        Review r = new Review();
        r.setReviewId(rs.getInt("review_id"));
        r.setAnswerId(rs.getInt("answer_id"));
        r.setReviewerUserName(rs.getString("reviewer_user"));
        r.setReviewText(rs.getString("review_text"));
        r.setRating(rs.getInt("rating"));
        Timestamp ts = rs.getTimestamp("creation_time");
        if (ts != null) {
            r.setCreationTime(ts.toLocalDateTime());
        }
        return r;
    }

    // ---------------- TRUSTED REVIEWERS ----------------

    public void addOrUpdateTrustedReviewer(String owner, String reviewer, int weight) {
        String checkSql = "SELECT * FROM cse360trusted_reviewers WHERE owner_user = ? AND reviewer_user = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setString(1, owner);
            checkStmt.setString(2, reviewer);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                String updateSql = "UPDATE cse360trusted_reviewers SET weight = ? WHERE owner_user = ? AND reviewer_user = ?";
                try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                    updateStmt.setInt(1, weight);
                    updateStmt.setString(2, owner);
                    updateStmt.setString(3, reviewer);
                    updateStmt.executeUpdate();
                }
            } else {
                String insertSql = "INSERT INTO cse360trusted_reviewers (owner_user, reviewer_user, weight) VALUES (?, ?, ?)";
                try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                    insertStmt.setString(1, owner);
                    insertStmt.setString(2, reviewer);
                    insertStmt.setInt(3, weight);
                    insertStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<TrustedReviewer> getTrustedReviewers(String owner) {
        List<TrustedReviewer> list = new ArrayList<>();
        String sql = "SELECT * FROM cse360trusted_reviewers WHERE owner_user = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, owner);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                TrustedReviewer tr = new TrustedReviewer();
                tr.setOwnerUserName(rs.getString("owner_user"));
                tr.setReviewerUserName(rs.getString("reviewer_user"));
                tr.setWeight(rs.getInt("weight"));
                list.add(tr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void removeTrustedReviewer(String owner, String reviewer) {
        String sql = "DELETE FROM cse360trusted_reviewers WHERE owner_user = ? AND reviewer_user = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, owner);
            pstmt.setString(2, reviewer);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---------------- SCORECARD PARAMETERS ----------------
    public void setScorecardParam(String paramName, int paramValue) {
        String sql = "MERGE INTO cse360scorecard_params KEY(param_name) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, paramName);
            pstmt.setInt(2, paramValue);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public int getScorecardParam(String paramName) {
        String sql = "SELECT param_value FROM cse360scorecard_params WHERE param_name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, paramName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("param_value");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ---------------- REQUESTS ----------------
    public int createRequest(String createdBy, String description) {
        String sql = "INSERT INTO cse360requests (created_by, description, status) VALUES (?, ?, 'OPEN')";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, createdBy);
            pstmt.setString(2, description);
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    public List<Request> getAllRequests() {
        List<Request> list = new ArrayList<>();
        String sql = "SELECT * FROM cse360requests ORDER BY request_id DESC";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                Request r = new Request();
                r.setRequestId(rs.getInt("request_id"));
                r.setCreatedBy(rs.getString("created_by"));
                r.setAssignedTo(rs.getString("assigned_to"));
                r.setDescription(rs.getString("description"));
                r.setStatus(rs.getString("status"));
                r.setNotes(rs.getString("notes"));
                int origId = rs.getInt("original_request_id");
                r.setOriginalRequestId(rs.wasNull() ? null : origId);
                list.add(r);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public void closeRequest(int requestId, String adminNotes) {
        String sql = "UPDATE cse360requests SET status = 'CLOSED', notes = ? WHERE request_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, adminNotes);
            pstmt.setInt(2, requestId);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void reopenRequest(int requestId, String newDescription) {
        String sql = "UPDATE cse360requests SET status = 'REOPENED', description = ? WHERE request_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newDescription);
            pstmt.setInt(2, requestId);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    // ---------------- ROLE ADDITION ----------------
    public void addRoleToUser(String userName, String newRole) {
        String sqlGet = "SELECT role FROM cse360users WHERE userName = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sqlGet)) {
            pstmt.setString(1, userName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String roles = rs.getString("role");
                if (roles == null || roles.isEmpty()) {
                    roles = newRole;
                } else if (!roles.contains(newRole)) {
                    roles += "," + newRole;
                }
                String sqlUpdate = "UPDATE cse360users SET role = ? WHERE userName = ?";
                try (PreparedStatement pstmt2 = connection.prepareStatement(sqlUpdate)) {
                    pstmt2.setString(1, roles);
                    pstmt2.setString(2, userName);
                    pstmt2.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
