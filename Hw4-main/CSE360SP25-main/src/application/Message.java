package application;

import java.time.LocalDateTime;

public class Message {
    private int messageId;
    private String fromUser;
    private String toUser;
    private int questionId;  // Optional: links to a specific question
    private String content;
    private LocalDateTime creationTime;
    private boolean isRead;

    public Message() {
        creationTime = LocalDateTime.now();
    }

    // Getters and setters
    public int getMessageId() { return messageId; }
    public void setMessageId(int messageId) { this.messageId = messageId; }

    public String getFromUser() { return fromUser; }
    public void setFromUser(String fromUser) { this.fromUser = fromUser; }

    public String getToUser() { return toUser; }
    public void setToUser(String toUser) { this.toUser = toUser; }

    public int getQuestionId() { return questionId; }
    public void setQuestionId(int questionId) { this.questionId = questionId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getCreationTime() { return creationTime; }
    public void setCreationTime(LocalDateTime creationTime) { this.creationTime = creationTime; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
}
