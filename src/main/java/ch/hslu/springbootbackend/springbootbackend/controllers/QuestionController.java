package ch.hslu.springbootbackend.springbootbackend.controllers;

import ch.hslu.springbootbackend.springbootbackend.Entity.Answer;
import ch.hslu.springbootbackend.springbootbackend.Entity.Question;
import ch.hslu.springbootbackend.springbootbackend.Exception.ResourceNotFoundException;
import ch.hslu.springbootbackend.springbootbackend.Repository.AnswerRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


// TODO: Wenn Controller mit der Zeit zu komplex/gross wird, Logik auf einen Service auslagern!!!!

@RestController
@RequestMapping("/api/v1")
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/questions")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    @PostMapping("/questions")
    @PreAuthorize("hasRole('ADMIN')")
    public Question newQuestion(@RequestBody Question newQuestion) {

        List<Question> questions = questionRepository.findByQuestionphrase(newQuestion.getQuestionphrase());
        if (!questions.isEmpty()) {
            System.out.println("Question already exists!!!");
            return questions.get(0);
        }
        for (Answer answer : newQuestion.getPossibleAnswers()) {
            List<Answer> answers = answerRepository.findByAnswerPhrase(answer.getAnswerPhrase());
            if (!answers.isEmpty()) {
                System.out.println("Answer already exists!!!!");
                // TODO: Replace the answer (in newQuestion) with the Answer which already exists!!!
            }
        }

        return questionRepository.save(newQuestion);
    }
    @GetMapping("/questions/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    public Question getQuestionById(@PathVariable(value = "id") Integer questionId) throws ResourceNotFoundException {

        Question question = questionRepository.findById(questionId).orElseThrow(
                () -> new ResourceNotFoundException("Question not found for this id :: " + questionId)
        );
        return question;
    }


}
