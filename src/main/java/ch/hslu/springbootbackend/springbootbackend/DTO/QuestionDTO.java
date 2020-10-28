package ch.hslu.springbootbackend.springbootbackend.DTO;

import ch.hslu.springbootbackend.springbootbackend.Entity.Answer;
import ch.hslu.springbootbackend.springbootbackend.Entity.Media;
import ch.hslu.springbootbackend.springbootbackend.Utils.QuestionType;
import org.springframework.hateoas.RepresentationModel;

import java.util.LinkedList;
import java.util.List;

public class QuestionDTO extends RepresentationModel<QuestionDTO> {

    private Integer id;

    private String questionphrase;

    private List<Answer> possibleAnswers = new LinkedList<>();

    private List<Answer> correctAnswers = new LinkedList<>();

    private QuestionType questionType;

    //private Set<Statistic> statistics = new HashSet<>();

    private Media questionImage;
    private Media answerImage;

    public QuestionDTO(Integer id, String questionphrase, QuestionType questionType, List<Answer> possibleAnswer, List<Answer> correctAnswer) {
        this.id = id;
        this.questionphrase = questionphrase;
        this.questionType = questionType;
        this.possibleAnswers = possibleAnswer;
        this.correctAnswers = correctAnswer;
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

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public List<Answer> getPossibleAnswers() {
        return possibleAnswers;
    }

    public void setPossibleAnswers(List<Answer> possibleAnswers) {
        this.possibleAnswers = possibleAnswers;
    }

    public List<Answer> getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(List<Answer> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    @Override
    public String toString() {
        return "QuestionDTO{" +
                "id=" + id +
                ", questionphrase='" + questionphrase + '\'' +
                ", possibleAnswers=" + possibleAnswers +
                ", correctAnswers=" + correctAnswers +
                ", questionType=" + questionType;
    }

    //private List<CategorySet> categorySet;
}
