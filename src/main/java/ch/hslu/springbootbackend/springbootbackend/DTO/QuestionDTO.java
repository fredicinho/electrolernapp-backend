package ch.hslu.springbootbackend.springbootbackend.DTO;

import ch.hslu.springbootbackend.springbootbackend.Entity.Answer;
import ch.hslu.springbootbackend.springbootbackend.Utils.QuestionType;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class QuestionDTO extends RepresentationModel<QuestionDTO> {

    @Id
    @NotNull
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private String questionphrase;
    private List<Answer> possibleAnswers = new LinkedList<>();
    private List<Answer> correctAnswers = new LinkedList<>();

    private QuestionType questionType;

    private int pointsToAchieve;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long userId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Integer> statisticsIds = new ArrayList<>();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Integer> categorySetIds = new ArrayList<>();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int questionImageId = 0;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int answerImageId= 0;

    public QuestionDTO(){}

    public QuestionDTO(Integer id, String questionphrase, QuestionType questionType, int pointsToAchieve) {
        this.id = id;
        this.questionphrase = questionphrase;
        this.questionType = questionType;
        this.pointsToAchieve = pointsToAchieve;
    }

    public QuestionDTO(String questionphrase, QuestionType questionType, List<Answer> possibleAnswer, List<Answer> correctAnswer, List<Integer> statisticsIds, int questionImageId, int answerImageId, int pointsToAchieve) {
        this.questionphrase = questionphrase;
        this.questionType = questionType;
        this.possibleAnswers = possibleAnswer;
        this.correctAnswers = correctAnswer;
        this.statisticsIds = statisticsIds;
        this.questionImageId = questionImageId;
        this.answerImageId = answerImageId;
        this.pointsToAchieve = pointsToAchieve;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestionphrase() {
        return questionphrase;
    }

    public void setQuestionphrase(String questionphrase) {
        this.questionphrase = questionphrase;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public List<Answer> getPossibleAnswers() {
        return possibleAnswers;
    }

    public void setPossibleAnswers(List<Answer> possibleAnswers) {
        this.possibleAnswers = possibleAnswers;
    }

    public List<Answer> getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(List<Answer> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public List<Integer> getStatisticsIds() {
        return statisticsIds;
    }

    public void setStatisticsIds(List<Integer> statisticsIds) {
        this.statisticsIds = statisticsIds;
    }

    public int getQuestionImageId() {
        return questionImageId;
    }

    public void setQuestionImageId(int questionImageId) {
        this.questionImageId = questionImageId;
    }

    public int getAnswerImageId() {
        return answerImageId;
    }

    public void setAnswerImageId(int answerImageId) {
        this.answerImageId = answerImageId;
    }

    public List<Integer> getCategorySetIds() {
        return categorySetIds;
    }

    public void setCategorySetIds(List<Integer> categorySetIds) {
        this.categorySetIds = categorySetIds;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getPointsToAchieve() {
        return pointsToAchieve;
    }

    public void setPointsToAchieve(int pointsToAchieve) {
        this.pointsToAchieve = pointsToAchieve;
    }

    @Override
    public String toString() {
        return "QuestionDTO{" +
                "id=" + id +
                ", questionphrase='" + questionphrase + '\'' +
                ", possibleAnswers=" + possibleAnswers +
                ", correctAnswers=" + correctAnswers +
                ", questionType=" + questionType;
    }

    //private List<CategorySet> categorySet;
}
