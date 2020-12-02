package ch.hslu.springbootbackend.springbootbackend.DTO;

public class ExamResultUpdateDTO {

    long userId;
    double pointsAchieved;
    int examSetId;
    int questionId;


    public ExamResultUpdateDTO(long userId, double pointsAchieved, int examSetId, int questionId) {
        this.userId = userId;
        this.pointsAchieved = pointsAchieved;
        this.examSetId = examSetId;
        this.questionId = questionId;
    }

    public ExamResultUpdateDTO() {
    }
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public double getPointsAchieved() {
        return pointsAchieved;
    }

    public void setPointsAchieved(double pointsAchieved) {
        this.pointsAchieved = pointsAchieved;
    }

    public int getExamSetId() {
        return examSetId;
    }

    public void setExamSetId(int examSetId) {
        this.examSetId = examSetId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }


    @Override
    public String toString() {
        return "ExamResultUpdateDTO{" +
                "userId=" + userId +
                ", pointsAchieved=" + pointsAchieved +
                ", examSetId=" + examSetId +
                ", questionId=" + questionId +
                '}';
    }
}
