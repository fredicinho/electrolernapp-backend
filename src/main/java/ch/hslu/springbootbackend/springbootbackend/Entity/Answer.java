package ch.hslu.springbootbackend.springbootbackend.Entity;

import javax.persistence.*;

@Entity
public class Answer{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer answer_id;

    @Column(length=1000000)
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
        return answer_id;
    }

    public void setId(Integer id) {
        this.answer_id = id;
    }
    @Override
    public String toString() {
        return "Answer{" +
                "answerPhrase='" + answerPhrase + '\'' +
                '}';
    }
}
