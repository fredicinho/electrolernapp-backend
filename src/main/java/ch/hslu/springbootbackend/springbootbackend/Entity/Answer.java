package ch.hslu.springbootbackend.springbootbackend.Entity;

import javax.persistence.*;

//comment
@Entity
public class Answer{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return "Answer{" +
                "answerPhrase='" + answerPhrase + '\'' +
                '}';
    }
}
