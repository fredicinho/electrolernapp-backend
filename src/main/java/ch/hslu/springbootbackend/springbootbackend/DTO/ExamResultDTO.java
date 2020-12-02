package ch.hslu.springbootbackend.springbootbackend.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ExamResultDTO extends RepresentationModel<ExamResultDTO> {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer examResultId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double pointsAchieved;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String username;

    private int questionId;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int examSetId;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<String> sendedAnswers = new LinkedList<>();

    private Date changedByTeacher = null;

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

    public Date getChangedByTeacher() {
        return changedByTeacher;
    }

    public void setChangedByTeacher(Date changedByTeacher) {
        this.changedByTeacher = changedByTeacher;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    @Override
    public String toString() {
        return "ExamResultDTO{" +
                "examResultId=" + examResultId +
                ", pointsAchieved=" + pointsAchieved +
                ", username='" + username + '\'' +
                ", questionId=" + questionId +
                ", examSetId=" + examSetId +
                ", sendedAnswers=" + sendedAnswers +
                ", changedByTeacher=" + changedByTeacher +
                '}';
    }
}
