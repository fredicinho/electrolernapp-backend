package ch.hslu.springbootbackend.springbootbackend.Entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "statistic")
public class Statistic{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer statisticId;
    private Date date;
    private int pointsAchieved;
    private boolean isMarked;

    @ManyToOne
    @JoinColumn(name="FK_userId", nullable=false)
    private User user;

    @ManyToOne
    @JoinColumn(name="FK_questionId", nullable=false)
    private Question question;

    public Statistic(Date date, int pointsAchieved, boolean isMarked) {
        this.date = date;
        this.pointsAchieved = pointsAchieved;
        this.isMarked = isMarked;
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

    public Statistic(){}

    public Integer getStatisticId() {
        return statisticId;
    }

    public void setStatisticId(Integer statisticId) {
        this.statisticId = statisticId;
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
