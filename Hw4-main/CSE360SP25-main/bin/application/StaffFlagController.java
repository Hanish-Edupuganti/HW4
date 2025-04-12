package application;

import java.util.List;

public class StaffFlagController {

    private FlaggedItemDAO flaggedItemDAO;

    public StaffFlagController() {
        flaggedItemDAO = new FlaggedItemDAO();
    }

    public List<Question> getFlaggedQuestions() {
        return flaggedItemDAO.fetchFlaggedQuestions();
    }

    public List<Answer> getFlaggedAnswers() {
        return flaggedItemDAO.fetchFlaggedAnswers();
    }

    public boolean resolveFlag(String itemType, int itemId) {
        return flaggedItemDAO.markFlagResolved(itemType, itemId);
    }

    public boolean deleteContent(String itemType, int itemId) {
        return flaggedItemDAO.removeContent(itemType, itemId);
    }

    public boolean addComment(int flagId, String comment, String staffUsername) {
        return flaggedItemDAO.addFlagComment(flagId, comment, staffUsername);
    }
}
