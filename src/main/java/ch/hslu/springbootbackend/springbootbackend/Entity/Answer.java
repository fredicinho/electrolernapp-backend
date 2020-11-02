package ch.hslu.springbootbackend.springbootbackend.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Answer extends RepresentationModel<Answer> {

    @JsonIgnore
    @Id
    @NotNull
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer answer_id;

    @Column(length=1000000)
    private String answerPhrase;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "question_possibleAnswer",
            joinColumns = @JoinColumn(name = "answerId"),
            inverseJoinColumns = @JoinColumn(name = "questionId"))
    private List<Question> questionPossibleList = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "question_correctAnswer",
            joinColumns = @JoinColumn(name = "answerId"),
            inverseJoinColumns = @JoinColumn(name = "questionId"))
    private List<Question> questionCorrectList = new ArrayList<>();

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

    public Integer getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(Integer answer_id) {
        this.answer_id = answer_id;
    }

    public List<Question> getQuestionPossibleList() {
        return questionPossibleList;
    }

    public void setQuestionPossibleList(List<Question> questionPossibleList) {
        this.questionPossibleList = questionPossibleList;
    }

    public List<Question> getQuestionCorrectList() {
        return questionCorrectList;
    }

    public void setQuestionCorrectList(List<Question> questionCorrectList) {
        this.questionCorrectList = questionCorrectList;
    }

    public void insertPossibleQuestion(Question question) {
        this.questionPossibleList.add(question);
    }
    public void insertCorrectQuestion(Question question) {
        this.questionCorrectList.add(question);
    }

    @Override
    public String toString() {
        return "Answer{" +
                "answerPhrase='" + answerPhrase + '\'' +
                '}';
    }
}
