package ch.hslu.springbootbackend.springbootbackend.controllers;

import ch.hslu.springbootbackend.springbootbackend.DTO.QuestionDTO;
import ch.hslu.springbootbackend.springbootbackend.Entity.Question;
import ch.hslu.springbootbackend.springbootbackend.Exception.ResourceNotFoundException;
import ch.hslu.springbootbackend.springbootbackend.Service.EntityService.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

//@CrossOrigin(origins = "http://localhost:80", maxAge = 3600)
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/questions")
public class QuestionController {
    private final Logger LOG = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    private QuestionService questionService;

    @GetMapping("")
    public List<QuestionDTO> getAllQuestions() {
        List<QuestionDTO> questions = new ArrayList<>();
        try {
            questions = questionService.getAllQuestions();
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        return questions;
    }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public String modAccess() {
        return "Mod Content.";
    }

    @GetMapping("/all")
    public String allAccess() {
        return "All Content.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String adminAccess() {
        return "Admin Content.";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String userAccess() {
        return "User Content.";
    }

    @PostMapping("")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public Question newQuestion(@RequestBody QuestionDTO newQuestion) {
        return questionService.createNewQuestion(newQuestion);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionDTO> getQuestionById(@PathVariable(value = "id") Integer questionId) {
        //LOG.warn(foundedQuestion.toString());

        try {
            QuestionDTO foundedQuestion = questionService.getById(questionId);
            LOG.warn(foundedQuestion.toString());

            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(foundedQuestion);
        } catch (ResourceNotFoundException ex) {
            LOG.warn("Resource was not found: " + ex.getMessage());
            return ResponseEntity.
                    notFound()
                    .build();
        }
    }

    @GetMapping("/categorySet")
    public List<QuestionDTO> getQuestionsByCategorySet(@RequestParam int categorySetId) {
        List<QuestionDTO> questionDTOS = new ArrayList<>();
        try {
            questionDTOS = questionService.getByCategorySetId(categorySetId);
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        return questionDTOS;
    }


}
