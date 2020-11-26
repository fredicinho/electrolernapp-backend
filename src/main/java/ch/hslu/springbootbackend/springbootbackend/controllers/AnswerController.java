package ch.hslu.springbootbackend.springbootbackend.controllers;

import ch.hslu.springbootbackend.springbootbackend.Entity.Answer;
import ch.hslu.springbootbackend.springbootbackend.Entity.ExamResult;
import ch.hslu.springbootbackend.springbootbackend.Entity.Question;
import ch.hslu.springbootbackend.springbootbackend.Repository.AnswerRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.ExamResultRepository;
import ch.hslu.springbootbackend.springbootbackend.Service.EntityService.QuestionService;
import ch.hslu.springbootbackend.springbootbackend.Strategy.DTOParserQuestion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
@RequestMapping("/api/v1/answers")
public class AnswerController {

    private final Logger LOG = LoggerFactory.getLogger(QuestionController.class);


    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuestionService questionService;

    @Autowired
    ExamResultRepository examResultRepository;
    @Autowired
    DTOParserQuestion dtoParserQuestion;

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

    @GetMapping("/examSet")
    public List<Answer> getAnswersByExamAnswer(@RequestParam int examAnswerId){
        List<Answer> answers = new LinkedList<>();
        Optional<ExamResult> examAnswerOptional = examResultRepository.findById(examAnswerId);
        if(examAnswerOptional.isPresent()){
            answers = answerRepository.findAllByExamResults(examAnswerOptional.get());
        }return answers;
    }

    public List<Answer> getPossibleAnswersByQuestion(@RequestParam int questionId){
        Question question = dtoParserQuestion.generateObjectFromDTO(questionService.getById(questionId));
        return answerRepository.findAnswersByQuestionPossibleList(question);
    }

    public List<Answer> getCorrectAnswersByQuestion(@RequestParam int questionId){
        Question question = dtoParserQuestion.generateObjectFromDTO(questionService.getById(questionId));
        return answerRepository.findAnswersByQuestionPossibleList(question);
    }
}
