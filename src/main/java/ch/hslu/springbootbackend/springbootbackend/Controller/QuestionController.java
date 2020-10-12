package ch.hslu.springbootbackend.springbootbackend.Controller;

import ch.hslu.springbootbackend.springbootbackend.Entity.Question;
import ch.hslu.springbootbackend.springbootbackend.Exception.ResourceNotFoundException;
import ch.hslu.springbootbackend.springbootbackend.Repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// TODO: Wenn Controller mit der Zeit zu komplex/gross wird, Logik auf einen Service auslagern!!!!

@CrossOrigin(origins = "http://localhost:80", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/questions")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    @PostMapping("/questions")
    @PreAuthorize("hasRole('ADMIN')")
    public Question newQuestion(@RequestBody Question newQuestion) {
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
