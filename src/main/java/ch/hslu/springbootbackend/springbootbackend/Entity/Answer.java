package ch.hslu.springbootbackend.springbootbackend.Entity;

import javax.persistence.*;

@Entity
public class Answer{

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    private String answerPhrase;

    public String getAnswerPhrase() {
        return answerPhrase;
    }

    public void setAnswerPhrase(String answerPhrase) {
        this.answerPhrase = answerPhrase;
    }

    public Answer() {}

    public Answer(String answerPhrase) {
        this.answerPhrase = answerPhrase;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "answerPhrase='" + answerPhrase + '\'' +
                '}';
    }
}
