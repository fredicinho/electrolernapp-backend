package ch.hslu.springbootbackend.springbootbackend.Service;

import ch.hslu.springbootbackend.springbootbackend.Entity.Answer;
import ch.hslu.springbootbackend.springbootbackend.Entity.Question;
import ch.hslu.springbootbackend.springbootbackend.Exception.ResourceNotFoundException;
import ch.hslu.springbootbackend.springbootbackend.Repository.AnswerRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerRepository answerRepository;


    public Question createNewQuestion(Question newQuestion) {

        List<Question> questions = questionRepository.findByQuestionphrase(newQuestion.getQuestionphrase());

        if (!questions.isEmpty()) {
            System.out.println("Question already exists!!!");
            return questions.get(0);
        }

        for (int i = 0; i < newQuestion.getPossibleAnswers().size(); i++) {
            Answer answer = checkIfAnswerExists(newQuestion.getPossibleAnswers().get(i).getAnswerPhrase());
            if (answer.equals(newQuestion.getPossibleAnswers().get(i))) {
            } else {
                newQuestion.getPossibleAnswers().set(i, answer);
            }
        }

        return questionRepository.save(newQuestion);
    }

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public Question getById(int questionId) throws ResourceNotFoundException {
        Question question = questionRepository.findById(questionId).orElseThrow(
                () -> new ResourceNotFoundException("Question not found for this id :: " + questionId)
        );

        return question;
    }

    private Answer checkIfAnswerExists(String answerPhrase) {
        List<Answer> foundedAnswer = answerRepository.findByAnswerPhrase(answerPhrase);
        if (foundedAnswer.isEmpty()) {
            return new Answer(answerPhrase);
        } else {
            return foundedAnswer.get(0);
        }
    }
}
