package ch.hslu.springbootbackend.springbootbackend.controllers;

import ch.hslu.springbootbackend.springbootbackend.Entity.Question;
import ch.hslu.springbootbackend.springbootbackend.Exception.ResourceNotFoundException;
import ch.hslu.springbootbackend.springbootbackend.Service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = "http://localhost:80", maxAge = 3600)
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/questions")
    //@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<Question> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    @PostMapping("/questions")
    @PreAuthorize("hasRole('ADMIN')")
    public Question newQuestion(@RequestBody Question newQuestion) {
        return questionService.createNewQuestion(newQuestion);
    }
    @GetMapping("/questions/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    public Question getQuestionById(@PathVariable(value = "id") Integer questionId) throws ResourceNotFoundException {
        return questionService.getById(questionId);
    }


}
