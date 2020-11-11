package ch.hslu.springbootbackend.springbootbackend.Entity;

import ch.hslu.springbootbackend.springbootbackend.Entity.Sets.ExamSet;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
public class ExamResult{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Double pointsAchieved;
    @ManyToOne
    @JoinColumn(name = "FK_userId", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "fk_question", nullable = false)
    private Question question;
    @ManyToOne
    @JoinColumn(name = "fk_examSet", nullable = false)
    private ExamSet examSet;
    @ManyToMany(mappedBy = "examResults")
    private List<Answer> answersToCheck = new LinkedList<>();

    public ExamResult(){}
    public ExamResult(Integer id, User user, Question question, ExamSet examSet, List<Answer> answersToCheck) {
        this.id = id;
        this.user = user;
        this.question = question;
        this.examSet = examSet;
        this.answersToCheck = answersToCheck;
    }


    @PostPersist
    private void assignFKs() {
        for (int i = 0; i < answersToCheck.size(); i++) {
            answersToCheck.get(i).insertExamQuestionSolutions(this);
        }
        if (this.question != null) {
            this.question.getExamResults().add(this);
        }
        if (this.user != null) {
            this.user.getExamResults().add(this);
        }
        if (this.examSet != null) {
            this.examSet.getExamResults().add(this);
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getPointsAchieved() {
        return pointsAchieved;
    }

    public void setPointsAchieved(Double pointsAchieved) {
        this.pointsAchieved = pointsAchieved;
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

    public List<Answer> getAnswersToCheck() {
        return answersToCheck;
    }

    public void setAnswersToCheck(List<Answer> answersToCheck) {
        this.answersToCheck = answersToCheck;
    }


    public ExamSet getExamSet() {
        return examSet;
    }

    public void setExamSet(ExamSet examSet) {
        this.examSet = examSet;
    }
}
