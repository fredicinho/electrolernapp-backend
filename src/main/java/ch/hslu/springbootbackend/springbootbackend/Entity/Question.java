package ch.hslu.springbootbackend.springbootbackend.Entity;

import java.util.LinkedList;
import java.util.List;
import javax.persistence.*;

@Entity
public class Question{

    public Question(){}
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    private String questionphrase;

    @OneToMany(targetEntity = Answer.class, cascade = CascadeType.ALL)
    private List<Answer> possibleAnswers = new LinkedList<>();

    @OneToOne(targetEntity = Answer.class, cascade = CascadeType.ALL)
    private Answer correctAnswer;

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

    public Question(String questionphrase, Answer[] answers) {
        this.questionphrase = questionphrase;
        for (Answer answer : answers
        ) {
            this.possibleAnswers.add(answer);

        }

    }


    @Override
    public String toString() {
        return "Question{" +
                "questionPhrase='" + questionphrase + '\'' +
                ", possibleAnswers=" + possibleAnswers +
                ", correctAnswer=" + correctAnswer +
                '}';
    }
}
