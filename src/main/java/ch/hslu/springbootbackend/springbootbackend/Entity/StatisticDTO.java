package ch.hslu.springbootbackend.springbootbackend.Entity;

import java.util.Date;

public class StatisticDTO {
    private Integer StatisticId;
    private Date date;
    private int pointsAchieved;
    private boolean isMarked;
    private long userId;
    private int questionId;

    public Integer getStatisticId() {
        return StatisticId;
    }

    public void setStatisticId(Integer statisticId) {
        StatisticId = statisticId;
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


    @Override
    public String toString() {
        return "StatisticDTO{" +
                "StatisticId=" + StatisticId +
                ", date=" + date +
                ", pointsAchieved=" + pointsAchieved +
                ", isMarked=" + isMarked +
                ", userId=" + userId +
                ", questionId=" + questionId +
                '}';
    }
}
