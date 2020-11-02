package ch.hslu.springbootbackend.springbootbackend.Entity;

import ch.hslu.springbootbackend.springbootbackend.Utils.QuestionType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class Question{

    public Question(){}

    @Id
    @NotNull
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(length=1000000)
    private String questionphrase;

    @ManyToMany(mappedBy = "questionPossibleList")
    private List<Answer> possibleAnswers = new LinkedList<>();

    @ManyToMany(mappedBy = "questionCorrectList")
    private List<Answer> correctAnswers = new LinkedList<>();

    private QuestionType questionType;

    @OneToMany(targetEntity = Statistic.class, cascade = CascadeType.ALL)
    private Set<Statistic> statistics = new HashSet<>();

    @OneToOne(targetEntity = Media.class, cascade = CascadeType.ALL)
    private Media questionImage;

    @OneToOne(targetEntity = Media.class, cascade = CascadeType.ALL)
    private Media answerImage;


    @ManyToMany(mappedBy = "questionsInSet")
    private List<CategorySet> categorySet;

    public Question(String questionPhrase, List<Answer> possibleAnswers, List<Answer> correctAnswers, QuestionType questionType, List<CategorySet> categorySet, Media questionImage, Media solutionImage) {
        this.setQuestionphrase(questionPhrase);
        this.setPossibleAnswers(possibleAnswers);
        this.setCorrectAnswers(correctAnswers);
        this.setQuestionType(questionType);
        this.setCategorySet(categorySet);
        this.setQuestionImage(questionImage);
        this.setAnswerImage(solutionImage);
    }

    @PostPersist
    private void addToCategorySet(){
        for(int i =0; i < categorySet.size(); i++){
            categorySet.get(i).insertQuestion(this);
        }
        for(int i =0; i < possibleAnswers.size(); i++){
            possibleAnswers.get(i).insertPossibleQuestion(this);
        }
        for(int i =0; i < correctAnswers.size(); i++){
            correctAnswers.get(i).insertCorrectQuestion(this);
        }
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

    public Question(String questionphrase, List<Answer> answers, Answer correctAnswer, QuestionType questionType) {
        this.setQuestionphrase(questionphrase);
        this.setPossibleAnswers(answers);
        this.setCorrectAnswers(correctAnswers);
        this.setQuestionType(questionType);
        this.setCategorySet(categorySet);
        this.setQuestionImage(questionImage);
        this.setAnswerImage(answerImage);
    }
    public Question(String questionphrase, List<Answer> answers, List<Answer> correctAnswers, QuestionType questionType, List<CategorySet> categorySet, Integer id, Media questionImage, Media answerImage) {
        this.setQuestionphrase(questionphrase);
        this.setPossibleAnswers(answers);
        this.setCorrectAnswers(correctAnswers);
        this.setQuestionType(questionType);
        this.setId(id);
        this.setCategorySet(categorySet);
        this.setQuestionImage(questionImage);
        this.setAnswerImage(answerImage);
    }

}
