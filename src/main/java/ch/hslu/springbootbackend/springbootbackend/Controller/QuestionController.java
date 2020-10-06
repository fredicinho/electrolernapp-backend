package ch.hslu.springbootbackend.springbootbackend.Controller;

import ch.hslu.springbootbackend.springbootbackend.Exception.ResourceNotFoundException;
import ch.hslu.springbootbackend.springbootbackend.Entity.Answer;
import ch.hslu.springbootbackend.springbootbackend.Entity.Question;
import ch.hslu.springbootbackend.springbootbackend.Repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

// TODO: Wenn Controller mit der Zeit zu komplex/gross wird, Logik auf einen Service auslagern!!!!

@RestController
@RequestMapping("/api/v1")
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/save")
    public String process(){
        Answer[] answers = {new Answer("how"), new Answer("how"), new Answer("how"), new Answer("how")};
        Question question = new Question("Whatever", answers);
        questionRepository.saveAll(Arrays.asList(question));
        return question.toString();
    }

    @GetMapping("/questions")
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    @GetMapping("/questions/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable(value = "id") Integer questionid) throws ResourceNotFoundException {
        Question question = questionRepository.findById(questionid).orElseThrow(
                () -> new ResourceNotFoundException("Question not found for this id :: " + questionid)
        );
        return ResponseEntity.ok().body(question);
    }

}
