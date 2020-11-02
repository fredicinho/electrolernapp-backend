package ch.hslu.springbootbackend.springbootbackend.controllers;

import ch.hslu.springbootbackend.springbootbackend.Entity.Answer;
import ch.hslu.springbootbackend.springbootbackend.Entity.Question;
import ch.hslu.springbootbackend.springbootbackend.Exception.ResourceNotFoundException;
import ch.hslu.springbootbackend.springbootbackend.Repository.AnswerRepository;
import ch.hslu.springbootbackend.springbootbackend.Service.EntityService.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/answers")
public class AnswerController {

    private final Logger LOG = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuestionService questionService;

    @GetMapping("/{id}")
    public ResponseEntity<Answer> getByAnswerId(@PathVariable(value = "id") Integer answerId) {
        //LOG.warn(foundedQuestion.toString());

            Optional<Answer> foundedAnswerOptional = answerRepository.findById(answerId);
            if(foundedAnswerOptional.isPresent()){
                Answer answer = foundedAnswerOptional.get();
                return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(answer);
            }else{
                return ResponseEntity.
                    notFound()
                    .build();
        }
    }

    public List<Answer> getPossibleAnswersByQuestion(@RequestParam int questionId) throws ResourceNotFoundException {
        Question question = questionService.generateQuestionFromQuestionDTO(questionService.getById(questionId));
        return answerRepository.findAnswersByQuestionPossibleList(question);
    }

    public List<Answer> getCorrectAnswersByQuestion(@RequestParam int questionId) throws ResourceNotFoundException {
        LOG.warn("peterpan");
        Question question = questionService.generateQuestionFromQuestionDTO(questionService.getById(questionId));
        return answerRepository.findAnswersByQuestionPossibleList(question);
    }
}
