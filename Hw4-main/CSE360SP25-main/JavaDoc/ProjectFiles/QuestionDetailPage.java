// File: QuestionDetailPage.java

public class QuestionDetailPage extends BorderPane {

    private Question question;
    private Questions questions;       // DB-backed manager for questions
    private Answers answersManager;    // DB-backed manager for answers
    private User currentUser;

    private ListView<Answer> answersListView;
    private ObservableList<Answer> answersList;

    public QuestionDetailPage(Question question, Questions questions, Answers answersManager, User currentUser) {
        this.question = question;
        this.questions = questions;
        this.answersManager = answersManager;
        this.currentUser = currentUser;
        initializeUI();
    }

    private void initializeUI() {
        setPadding(new Insets(10));

        // --- Top Section: Question details
        VBox questionDetails = new VBox(6);
        questionDetails.setPadding(new Insets(10));

        Label titleLabel = new Label(question.getQuestionTitle());
        titleLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        Label textLabel = new Label(question.getQuestionText());
        textLabel.setWrapText(true);

        Label authorLabel = new Label("Author: " + question.getAuthor());
        Label dateLabel = new Label("Date: " + question.getCreationTime().toLocalDate().toString());
        Label solvedLabel = new Label(question.isSolved() ? "This question is answered." : "Not answered yet");

        // Allow editing if admin or if current user is the question author.
        boolean canEditQuestion = "admin".equalsIgnoreCase(currentUser.getRole())
                || question.getAuthor().equals(currentUser.getUserName());
        if (canEditQuestion) {
            Button editQuestionBtn = new Button("Edit Question");
            editQuestionBtn.setOnAction(e -> editQuestion());
            questionDetails.getChildren().add(editQuestionBtn);
        }

        questionDetails.getChildren().addAll(
                titleLabel,
                textLabel,
                authorLabel,
                dateLabel,
                solvedLabel
        );
        setTop(questionDetails);

        // --- Center Section: List of Answers
        List<Answer> loadedAnswers = answersManager.getAnswersByQuestionID(question.getQuestionID());
        answersList = FXCollections.observableArrayList(loadedAnswers);
        answersListView = new ListView<>(answersList);
        answersListView.setPrefHeight(300);

        // Pass currentUser to the cell so we can show the Reviews button if appropriate.
        answersListView.setCellFactory(listView -> new AnswerListCell(answersManager, this, currentUser));

        answersListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Answer selected = answersListView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    handleAnswerAction(selected);
                }
            }
        });
        setCenter(answersListView);

        // --- Bottom Section: Add Answer and Extra Actions
        VBox bottomBox = new VBox(10);
        bottomBox.setPadding(new Insets(10));

        if (!question.isSolved()) {
            GridPane addAnswerPane = new GridPane();
            addAnswerPane.setHgap(10);
            addAnswerPane.setVgap(10);

            Label lblNewAnswer = new Label("Your Answer:");
            TextArea txtAnswerContent = new TextArea();
            txtAnswerContent.setPrefRowCount(3);

            Button addAnswerBtn = new Button("Add Answer");
            addAnswerBtn.setOnAction(e -> {
                String content = txtAnswerContent.getText();
                if (content == null || content.isBlank()) {
                    return;
                }
                Answer newAnswer = new Answer();
                newAnswer.setQuestionID(question.getQuestionID());
                newAnswer.setAuthor(currentUser.getUserName());
                newAnswer.setAnswerText(content);

                answersManager.addAnswer(newAnswer);
                refreshAnswerList();
                txtAnswerContent.clear();
            });

            addAnswerPane.add(lblNewAnswer, 0, 0);
            addAnswerPane.add(txtAnswerContent, 1, 0);
            addAnswerPane.add(addAnswerBtn, 1, 1);
            bottomBox.getChildren().add(addAnswerPane);
        }
        
        // Extra actions: Clone Question and Private Feedback
        HBox extraButtons = new HBox(10);
        Button cloneBtn = new Button("Clone Question");
        cloneBtn.setOnAction(e -> {
            Question newQ = new Question(question.getAuthor(), "[CLONE] " + question.getQuestionTitle(), question.getQuestionText());
            questions.addQuestion(newQ);
            new QuestionDetailPage(newQ, questions, answersManager, currentUser)
                    .show((Stage) getScene().getWindow());
        });
        
        Button feedbackBtn = new Button("Private Feedback");
        feedbackBtn.setOnAction(e -> {
            new PrivateFeedbackPage(StartCSE360.getDatabaseHelper(), question, currentUser)
                    .show((Stage) getScene().getWindow());
        });
        extraButtons.getChildren().addAll(cloneBtn, feedbackBtn);

        // Back button
        Button backButton = new Button("Back to Questions");
        backButton.setOnAction(e -> {
            new QuestionsPage(questions, answersManager, currentUser)
                    .show((Stage) getScene().getWindow());
        });
        
        bottomBox.getChildren().addAll(extraButtons, backButton);
        setBottom(bottomBox);
    }

    private void editQuestion() {
        TextField txtNewTitle = new TextField(question.getQuestionTitle());
        TextField txtNewContent = new TextField(question.getQuestionText());
        Button saveBtn = new Button("Save Changes");

        VBox editPane = new VBox(6,
                new Label("Edit Question:"),
                txtNewTitle,
                txtNewContent,
                saveBtn
        );
        editPane.setPadding(new Insets(10));
        setCenter(editPane);

        saveBtn.setOnAction(e -> {
            question.setQuestionTitle(txtNewTitle.getText());
            question.setQuestionText(txtNewContent.getText());
            questions.updateQuestion(question);
            new QuestionDetailPage(question, questions, answersManager, currentUser)
                    .show((Stage) getScene().getWindow());
        });
    }

    private void handleAnswerAction(Answer answer) {
        boolean isAdmin = "admin".equalsIgnoreCase(currentUser.getRole());
        boolean isAuthorOfAnswer = answer.getAuthor().equals(currentUser.getUserName());
        boolean isAuthorOfQuestion = question.getAuthor().equals(currentUser.getUserName());

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Answer Actions");

        ButtonType editBtnType = new ButtonType("Edit", ButtonBar.ButtonData.OK_DONE);
        ButtonType acceptBtnType = new ButtonType("Mark as Accepted", ButtonBar.ButtonData.APPLY);
        ButtonType reviewBtnType = new ButtonType("Reviews", ButtonBar.ButtonData.OTHER);
        ButtonType cancelBtnType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        if (isAdmin || isAuthorOfAnswer) {
            dialog.getDialogPane().getButtonTypes().add(editBtnType);
        }
        if (!question.isSolved() && (isAdmin || isAuthorOfQuestion)) {
            dialog.getDialogPane().getButtonTypes().add(acceptBtnType);
        }
        if (currentUser.getRoles().contains("reviewer")) {
            dialog.getDialogPane().getButtonTypes().add(reviewBtnType);
        }
        dialog.getDialogPane().getButtonTypes().add(cancelBtnType);
        dialog.getDialogPane().setContent(new Label("Choose an action for this answer."));

        dialog.showAndWait().ifPresent(response -> {
            if (response == editBtnType) {
                editAnswer(answer);
            } else if (response == acceptBtnType) {
                acceptAnswer(answer);
            } else if (response == reviewBtnType) {
                new ReviewManagementPage(StartCSE360.getDatabaseHelper(), answer, currentUser)
                        .show((Stage) getScene().getWindow());
            }
        });
    }

    private void editAnswer(Answer answer) {
        TextInputDialog editDialog = new TextInputDialog(answer.getAnswerText());
        editDialog.setTitle("Edit Answer");
        editDialog.setHeaderText("Update your answer text");
        editDialog.setContentText("New Answer Text:");
        editDialog.showAndWait().ifPresent(newText -> {
            answer.setAnswerText(newText);
            answersManager.updateAnswer(answer);
            refreshAnswerList();
        });
    }

    private void acceptAnswer(Answer answer) {
        question.setSolved(true);
        question.setAcceptedAnswerID(answer.getAnswerID());
        questions.updateQuestion(question);
        answer.setAccepted(true);
        answersManager.updateAnswer(answer);
        new QuestionDetailPage(question, questions, answersManager, currentUser)
                .show((Stage) getScene().getWindow());
    }

    void refreshAnswerList() {
        List<Answer> updated = answersManager.getAnswersByQuestionID(question.getQuestionID());
        answersList.setAll(updated);
    }

    public void show(Stage primaryStage) {
        Scene scene = new Scene(this, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Question Detail");
        primaryStage.show();
    }

    // Updated AnswerListCell to include a Reviews button if the user is a reviewer.
    private static class AnswerListCell extends ListCell<Answer> {
        private final Answers answersManager;
        private final QuestionDetailPage parentPage;
        private final User currentUser;

        private Label answerLabel;
        private Label votesLabel;
        private Button upvoteButton;
        private Button downvoteButton;
        private Button reviewsButton;
        private HBox rootLayout;

        public AnswerListCell(Answers answersManager, QuestionDetailPage parentPage, User currentUser) {
            this.answersManager = answersManager;
            this.parentPage = parentPage;
            this.currentUser = currentUser;

            answerLabel = new Label();
            votesLabel = new Label();

            upvoteButton = new Button("▲");
            upvoteButton.setOnAction(e -> {
                Answer ans = getItem();
                if (ans != null) {
                    ans.upvote();
                    answersManager.updateAnswer(ans);
                    parentPage.refreshAnswerList();
                }
            });

            downvoteButton = new Button("▼");
            downvoteButton.setOnAction(e -> {
                Answer ans = getItem();
                if (ans != null) {
                    ans.downvote();
                    answersManager.updateAnswer(ans);
                    parentPage.refreshAnswerList();
                }
            });

            reviewsButton = new Button("Reviews");
            reviewsButton.setOnAction(e -> {
                Answer ans = getItem();
                if (ans != null) {
                    new ReviewManagementPage(StartCSE360.getDatabaseHelper(), ans, currentUser)
                            .show((Stage) getScene().getWindow());
                }
            });
            HBox buttonsBox;
            if (currentUser.getRoles().contains("reviewer")) {
                buttonsBox = new HBox(5, upvoteButton, downvoteButton, reviewsButton);
            } else {
                buttonsBox = new HBox(5, upvoteButton, downvoteButton);
            }

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            rootLayout = new HBox(10, answerLabel, spacer, votesLabel, buttonsBox);
            rootLayout.setPadding(new Insets(5));
        }

        @Override
        protected void updateItem(Answer answer, boolean empty) {
            super.updateItem(answer, empty);
            if (empty || answer == null) {
                setText(null);
                setGraphic(null);
            } else {
                answerLabel.setText(answer.toString());
                votesLabel.setText(answer.getUpvotes() + "▲, " + answer.getDownvotes() + "▼");
                setGraphic(rootLayout);
                setText(null);
            }
        }
    }
}