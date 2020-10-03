package ch.hslu.springbootbackend.springbootbackend.Entity;

import javax.persistence.*;

@Entity
@Table(name = "answer")
public class Answer extends AbstractEntity {

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
