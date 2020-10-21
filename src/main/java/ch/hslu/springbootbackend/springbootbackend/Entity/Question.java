package ch.hslu.springbootbackend.springbootbackend.Entity;

import ch.hslu.springbootbackend.springbootbackend.Utils.QuestionType;

import java.util.LinkedList;
import java.util.List;
import javax.persistence.*;

@Entity
public class Question{

    public Question(){}

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(targetEntity = CategorySet.class, cascade = CascadeType.ALL)
    private CategorySet categorySet;

    private String questionphrase;

    @OneToMany(targetEntity = Answer.class, cascade = CascadeType.ALL)
    private List<Answer> possibleAnswers = new LinkedList<>();

    @OneToOne(targetEntity = Answer.class, cascade = CascadeType.ALL)
    private Answer correctAnswer;

    private QuestionType questionType;

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

    public Answer getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(Answer correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public CategorySet getCategorySet() {
        return categorySet;
    }

    public void setCategorySet(CategorySet categorySet) {
        this.categorySet = categorySet;
    }

    public Question(String questionphrase, List<Answer> answers, Answer correctAnswer, QuestionType questionType, CategorySet categorySet) {
        this.setQuestionphrase(questionphrase);
        this.setPossibleAnswers(answers);
        this.setCorrectAnswer(correctAnswer);
        this.setQuestionType(questionType);
        this.setCategorySet(categorySet);
    }
    public Question(String questionphrase, List<Answer> answers, Answer correctAnswer, QuestionType questionType, CategorySet categorySet, int id) {
        this.setQuestionphrase(questionphrase);
        this.setPossibleAnswers(answers);
        this.setCorrectAnswer(correctAnswer);
        this.setQuestionType(questionType);
        this.setId(id);
        this.setCategorySet(categorySet);
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", questionphrase='" + questionphrase + '\'' +
                ", possibleAnswers=" + possibleAnswers +
                ", correctAnswer=" + correctAnswer +
                ", questionType=" + questionType +
                '}';
    }
}
