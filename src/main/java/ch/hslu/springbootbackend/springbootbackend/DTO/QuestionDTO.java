package ch.hslu.springbootbackend.springbootbackend.DTO;

import ch.hslu.springbootbackend.springbootbackend.Entity.Answer;
import ch.hslu.springbootbackend.springbootbackend.Entity.QuestionLevel;
import ch.hslu.springbootbackend.springbootbackend.Entity.QuestionType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class QuestionDTO extends RepresentationModel<QuestionDTO> {

    @Id
    @NotNull
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private String questionPhrase;
    private List<Answer> possibleAnswers = new LinkedList<>();
    private List<Answer> correctAnswers = new LinkedList<>();

    private QuestionType questionType;

    private double pointsToAchieve;

    @JsonIgnore
    private String createdBy;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Integer> statisticsIds = new ArrayList<>();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Integer> categorySetIds = new ArrayList<>();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int questionImageId = 0;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int answerImageId= 0;

    private List<Integer> professionsList = new ArrayList<>();


    private QuestionLevel questionLevel;

    public QuestionDTO(){}

    public QuestionDTO(Integer id, String questionPhrase, QuestionType questionType, double pointsToAchieve, QuestionLevel questionLevel) {
        this.id = id;
        this.questionPhrase = questionPhrase;
        this.questionType = questionType;
        this.pointsToAchieve = pointsToAchieve;
        this.questionLevel = questionLevel;
    }

    public QuestionDTO(String questionPhrase, QuestionType questionType, List<Answer> possibleAnswer, List<Answer> correctAnswer, List<Integer> statisticsIds, int questionImageId, int answerImageId, double pointsToAchieve, QuestionLevel questionLevel) {
        this.questionPhrase = questionPhrase;
        this.questionType = questionType;
        this.possibleAnswers = possibleAnswer;
        this.correctAnswers = correctAnswer;
        this.statisticsIds = statisticsIds;
        this.questionImageId = questionImageId;
        this.answerImageId = answerImageId;
        this.pointsToAchieve = pointsToAchieve;
        this.questionLevel = questionLevel;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestionPhrase() {
        return questionPhrase;
    }

    public void setQuestionPhrase(String questionPhrase) {
        this.questionPhrase = questionPhrase;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public double getPointsToAchieve() {
        return pointsToAchieve;
    }

    public void setPointsToAchieve(double pointsToAchieve) {
        this.pointsToAchieve = pointsToAchieve;
    }

    public QuestionLevel getQuestionLevel() {
        return questionLevel;
    }

    public void setQuestionLevel(QuestionLevel questionLevel) {
        this.questionLevel = questionLevel;
    }
    public List<Integer> getProfessionsList() {
        return professionsList;
    }

    public void setProfessionsList(List<Integer> professionsList) {
        this.professionsList = professionsList;
    }



    @Override
    public String toString() {
        return "QuestionDTO{" +
                "id=" + id +
                ", questionphrase='" + questionPhrase + '\'' +
                ", possibleAnswers=" + possibleAnswers +
                ", correctAnswers=" + correctAnswers +
                ", questionType=" + questionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        QuestionDTO that = (QuestionDTO) o;
        return pointsToAchieve == that.pointsToAchieve &&
                Objects.equals(questionPhrase, that.questionPhrase) &&
                questionType == that.questionType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionPhrase, questionType, pointsToAchieve);
    }



    //private List<CategorySet> categorySet;
}
