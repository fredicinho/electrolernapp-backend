package ch.hslu.springbootbackend.springbootbackend.Entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "statistic")
public class Statistic{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer StatisticId;
    private Date date;
    private int pointsAchieved;
    private boolean isMarked;

    @ManyToOne
    @JoinColumn(name="FK_userId", nullable=false)
    private User user;

    @ManyToOne
    @JoinColumn(name="FK_questionId", nullable=false)
    private Question question;

    public Statistic(int pointsAchieved, boolean isMarked, User user, Question question) {
        this.pointsAchieved = pointsAchieved;
        this.isMarked = isMarked;
        this.user = user;
        this.question = question;
    }

    @PrePersist
    void createdAt() {
        this.date = new Date();
    }

    @PostPersist
    public void assignToFks(){
        this.question.getStatistics().add(this);
        this.user.getStatistics().add(this);
    }
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }



}
