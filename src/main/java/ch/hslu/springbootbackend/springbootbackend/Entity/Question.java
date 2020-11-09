package ch.hslu.springbootbackend.springbootbackend.Entity;

import ch.hslu.springbootbackend.springbootbackend.Entity.Sets.CategorySet;
import ch.hslu.springbootbackend.springbootbackend.Entity.Sets.ExamSet;
import ch.hslu.springbootbackend.springbootbackend.Utils.QuestionType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class Question{

    public Question(){}

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(length=1000000)
    private String questionphrase;

    @ManyToMany(mappedBy = "questionPossibleList")
    private List<Answer> possibleAnswers = new LinkedList<>();

    @ManyToMany(mappedBy = "questionCorrectList")
    private List<Answer> correctAnswers = new LinkedList<>();

    private QuestionType questionType;

    private int pointsToAchieve;

    @ManyToOne(targetEntity = User.class, cascade = CascadeType.ALL)
    private User createdByUser;

    @OneToMany(targetEntity = Statistic.class, cascade = CascadeType.ALL)
    private Set<Statistic> statistics = new HashSet<>();

    @OneToOne(targetEntity = Media.class, cascade = CascadeType.ALL)
    private Media questionImage;

    @OneToOne(targetEntity = Media.class, cascade = CascadeType.ALL)
    private Media answerImage;


    @ManyToMany(mappedBy = "questionsInSet")
    private List<CategorySet> categorySet = new LinkedList<>();


    @ManyToMany(mappedBy = "questionsInExamSet")
    private List<ExamSet> examSets = new LinkedList<>();


    public Question(String questionphrase, List<Answer> possibleAnswers, List<Answer> correctAnswers, QuestionType questionType, User user, List<CategorySet> categorySets, Media questionImage, Media answerImage, int pointsToAchieve) {
        this.questionphrase = questionphrase;
        this.possibleAnswers = possibleAnswers;
        this.correctAnswers = correctAnswers;
        this.questionType = questionType;
        this.createdByUser = user;
        this.questionImage = questionImage;
        this.answerImage = answerImage;
        this.categorySet = categorySets;
        this.pointsToAchieve = pointsToAchieve;
    }
    public Question(String questionphrase, List<Answer> answers, List<Answer>  correctAnswers, QuestionType questionType, int pointsToAchieve) {
        this.setQuestionphrase(questionphrase);
        this.setPossibleAnswers(answers);
        this.setCorrectAnswers(correctAnswers);
        this.setQuestionType(questionType);
        this.setPointsToAchieve(pointsToAchieve);
    }
    @PostPersist
    private void assignFKs(){
        for(int i =0; i < categorySet.size(); i++){
            categorySet.get(i).insertQuestion(this);
        }
        for(int i =0; i < possibleAnswers.size(); i++){
            possibleAnswers.get(i).insertPossibleQuestion(this);
        }
        for(int i =0; i < correctAnswers.size(); i++){
            correctAnswers.get(i).insertCorrectQuestion(this);
        }
        for(int i =0; i < examSets.size(); i++){
            examSets.get(i).insertQuestion(this);
        }
        if(this.createdByUser != null){
            this.createdByUser.getCreatedQuestions().add(this);
        }
    }

    public void insertIntoExamSet(ExamSet examSet){
        this.examSets.add(examSet);
    }


    public List<Answer> getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(List<Answer> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public Media getQuestionImage() {
        return questionImage;
    }

    public void setQuestionImage(Media questionImage) {
        this.questionImage = questionImage;
    }

    public Media getAnswerImage() {
        return answerImage;
    }

    public void setAnswerImage(Media answerImage) {
        this.answerImage = answerImage;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
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

    public List<Answer> getPossibleAnswers() {
        return possibleAnswers;
    }

    public void setPossibleAnswers(List<Answer> possibleAnswers) {
        this.possibleAnswers = possibleAnswers;
    }

    public List<CategorySet> getCategorySet() {
        return categorySet;
    }

    public void setCategorySet(List<CategorySet> categorySet) {
        this.categorySet = categorySet;
    }


    public Set<Statistic> getStatistics() {
        return statistics;
    }

    public void setStatistics(Set<Statistic> statistics) {
        this.statistics = statistics;
    }

    public User getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(User createdByUser) {
        this.createdByUser = createdByUser;
    }

    public int getPointsToAchieve() {
        return pointsToAchieve;
    }

    public void setPointsToAchieve(int pointsToAchieve) {
        this.pointsToAchieve = pointsToAchieve;
    }

    public List<ExamSet> getExamSets() {
        return examSets;
    }

    public void setExamSets(List<ExamSet> examSets) {
        this.examSets = examSets;
    }


    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", questionphrase='" + questionphrase + '\'' +
                ", possibleAnswers=" + possibleAnswers +
                ", correctAnswers=" + correctAnswers +
                ", questionType=" + questionType +
                ", pointsToAchieve=" + pointsToAchieve +
                ", createdByUser=" + createdByUser +
                ", statistics=" + statistics +
                ", questionImage=" + questionImage +
                ", answerImage=" + answerImage +
                ", categorySet=" + categorySet +
                '}';
    }
}
