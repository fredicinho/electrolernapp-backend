package ch.hslu.springbootbackend.springbootbackend.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class StatisticDTO extends RepresentationModel<StatisticDTO> {
    private final int statisticId;
    private Date date;
    private int pointsAchieved;
    private boolean isMarked;

    @JsonIgnore
    @NotNull
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    private int questionId;

    public StatisticDTO(int statisticId, Date date, int pointsAchieved, boolean isMarked) {
        this.statisticId = statisticId;
        this.date = date;
        this.pointsAchieved = pointsAchieved;
        this.isMarked = isMarked;
    }

    public int getStatisticId() {
        return statisticId;
    }

    public void setStatisticId(int statisticId) {
        statisticId = statisticId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getPointsAchieved() {
        return pointsAchieved;
    }

    public void setPointsAchieved(int pointsAchieved) {
        this.pointsAchieved = pointsAchieved;
    }

    public boolean isMarked() {
        return isMarked;
    }

    public void setMarked(boolean marked) {
        isMarked = marked;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }


    @Override
    public String toString() {
        return "StatisticDTO{" +
                "StatisticId=" + statisticId +
                ", date=" + date +
                ", pointsAchieved=" + pointsAchieved +
                ", isMarked=" + isMarked +
                ", username=" + username +
                ", questionId=" + questionId +
                '}';
    }
}
