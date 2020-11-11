package ch.hslu.springbootbackend.springbootbackend.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.RepresentationModel;

import java.util.LinkedList;
import java.util.List;

public class ExamResultDTO extends RepresentationModel<ExamResultDTO> {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer examResultId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double pointsAchieved;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private long userId;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int questionId;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int examSetId;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<String> sendedAnswers = new LinkedList<>();

    public ExamResultDTO(){}
    public ExamResultDTO(int id){
        this.examResultId = id;
    }
    public Integer getExamResultId() {
        return examResultId;
    }

    public void setExamResultId(Integer examResultId) {
        this.examResultId = examResultId;
    }

    public Double getPointsAchieved() {
        return pointsAchieved;
    }

    public void setPointsAchieved(Double pointsAchieved) {
        this.pointsAchieved = pointsAchieved;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getExamSetId() {
        return examSetId;
    }

    public void setExamSetId(int examSetId) {
        this.examSetId = examSetId;
    }

    public List<String> getSendedAnswers() {
        return sendedAnswers;
    }

    public void setSendedAnswers(List<String> sendedAnswers) {
        this.sendedAnswers = sendedAnswers;
    }

}
