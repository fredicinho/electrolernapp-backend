package ch.hslu.springbootbackend.springbootbackend.Controller;

import ch.hslu.springbootbackend.springbootbackend.Exception.ResourceNotFoundException;
import ch.hslu.springbootbackend.springbootbackend.Entity.Answer;
import ch.hslu.springbootbackend.springbootbackend.Entity.Question;
import ch.hslu.springbootbackend.springbootbackend.Repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

// TODO: Wenn Controller mit der Zeit zu komplex/gross wird, Logik auf einen Service auslagern!!!!

@CrossOrigin(origins = "http://localhost:80", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/save")
    public String process(){
        List<Answer> answers = new LinkedList<>();
        answers.add(new Answer("how"));
        answers.add(new Answer("are"));
        answers.add(new Answer("you"));
        Question question = new Question("Whatever", answers, answers.get(1));
        Question question2 = new Question("Hello", answers, answers.get(1));
        Question question3 = new Question("Ursula", answers, answers.get(1));
        Question question4 = new Question("Pterpan", answers, answers.get(1));
        questionRepository.saveAll(Arrays.asList(question));
        return question.toString();
    }

    @GetMapping("/questions")
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    @PostMapping("/questions")
    public Question newQuestion(@RequestBody Question newQuestion) {
        return questionRepository.save(newQuestion);
    }
    @GetMapping("/questions/{id}")
    public Question getQuestionById(@PathVariable(value = "id") Integer questionId) throws ResourceNotFoundException {
        Question question = questionRepository.findById(questionId).orElseThrow(
                () -> new ResourceNotFoundException("Question not found for this id :: " + questionId)
        );
        return question;
    }

}
