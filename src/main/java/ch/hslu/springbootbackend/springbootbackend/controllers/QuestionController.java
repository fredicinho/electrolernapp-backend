package ch.hslu.springbootbackend.springbootbackend.controllers;

import ch.hslu.springbootbackend.springbootbackend.DTO.QuestionDTO;
import ch.hslu.springbootbackend.springbootbackend.Exception.ResourceNotFoundException;
import ch.hslu.springbootbackend.springbootbackend.Service.EntityService.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @PreAuthorize("hasAnyRole()")
    @GetMapping("")
    public List<QuestionDTO> getAllQuestions() {
        List<QuestionDTO> questions = new ArrayList<>();
        questions = questionService.getAllQuestions();
        return questions;
    }


    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<QuestionDTO> newQuestion(@RequestBody QuestionDTO newQuestion) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        newQuestion.setCreatedBy(auth.getName());
            QuestionDTO questionDTO = questionService.createNewQuestion(newQuestion);
            if(questionDTO != null) {
                if(questionService.ressourceExists().get()){
                    questionService.ressourceExists().set(true);
                    return ResponseEntity
                            .status(HttpStatus.CONFLICT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(questionDTO);
                }else
                     return ResponseEntity
                            .status(HttpStatus.CREATED)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(questionDTO);
            }
            else{
                return ResponseEntity.
                        notFound()
                        .build();
            }
    }
    @PreAuthorize("hasAnyRole()")
    @GetMapping("/{id}")
    public ResponseEntity<QuestionDTO> getQuestionById(@PathVariable(value = "id") Integer questionId) {
        //LOG.warn(foundedQuestion.toString());
            QuestionDTO foundedQuestion = questionService.getById(questionId);
            if(foundedQuestion != null) {
                return ResponseEntity
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(foundedQuestion);
            }else {
                return ResponseEntity.
                        notFound()
                        .build();
            }
    }

    @PreAuthorize("hasAnyRole()")
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

    @PreAuthorize("hasRole('ROLE_EXAM') or hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/examSet")
    public List<QuestionDTO> getQuestionsByExamSet(@RequestParam int examSetId) {
        return questionService.getByExamSet(examSetId);
    }

    @GetMapping("/userId")
    public List<QuestionDTO> getQuestionByUserId(@RequestParam long userId) {
        List<QuestionDTO> questionDTOS = new ArrayList<>();
        try {
            questionDTOS = questionService.getByUserId(userId);
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        return questionDTOS;
    }


}
