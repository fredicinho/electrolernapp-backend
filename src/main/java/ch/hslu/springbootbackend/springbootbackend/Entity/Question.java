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

    public Question(String questionphrase, List<Answer> answers, Answer correctAnswer, QuestionType questionType) {
        this.setQuestionphrase(questionphrase);
        this.setPossibleAnswers(answers);
        this.setCorrectAnswer(correctAnswer);
        this.setQuestionType(questionType);
    }
    public Question(String questionphrase, List<Answer> answers, Answer correctAnswer, QuestionType questionType, int id) {
        this.setQuestionphrase(questionphrase);
        this.setPossibleAnswers(answers);
        this.setCorrectAnswer(correctAnswer);
        this.setQuestionType(questionType);
        this.setId(id);
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
