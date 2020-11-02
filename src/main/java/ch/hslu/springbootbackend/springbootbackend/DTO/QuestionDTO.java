package ch.hslu.springbootbackend.springbootbackend.DTO;

import ch.hslu.springbootbackend.springbootbackend.Entity.Answer;
import ch.hslu.springbootbackend.springbootbackend.Utils.QuestionType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class QuestionDTO extends RepresentationModel<QuestionDTO> {

    private Integer id;

    private String questionphrase;
    private List<Answer> possibleAnswers = new LinkedList<>();
    private List<Answer> correctAnswers = new LinkedList<>();

    private QuestionType questionType;
    @JsonIgnore
    private List<Integer> statisticsIds = new ArrayList<>();
    @JsonIgnore
    private List<Integer> categorySetIds = new ArrayList<>();
    @JsonIgnore
    private int questionImageId;
    @JsonIgnore
    private int answerImageId;

    public QuestionDTO(){};
    public QuestionDTO(Integer id, String questionphrase, QuestionType questionType) {
        this.id = id;
        this.questionphrase = questionphrase;
        this.questionType = questionType;
    }

    public QuestionDTO(String questionphrase, QuestionType questionType, List<Answer> possibleAnswer, List<Answer> correctAnswer, List<Integer> statisticsIds, int questionImageId, int answerImageId ) {
        this.questionphrase = questionphrase;
        this.questionType = questionType;
        this.possibleAnswers = possibleAnswer;
        this.correctAnswers = correctAnswer;
        this.statisticsIds = statisticsIds;
        this.questionImageId = questionImageId;
        this.answerImageId = answerImageId;
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
