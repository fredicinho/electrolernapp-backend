package ch.hslu.springbootbackend.springbootbackend.controllers;

import ch.hslu.springbootbackend.springbootbackend.DTO.ExamResultDTO;
import ch.hslu.springbootbackend.springbootbackend.Service.EntityService.ExamResultService;
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

import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/examResults")
public class ExamResultController {

    @Autowired
    ExamResultService examResultService;

    private final Logger LOG = LoggerFactory.getLogger(QuestionController.class);

    @PutMapping("/check")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity check(@RequestBody ExamResultDTO newExamResultDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        LOG.warn(String.valueOf(auth.getAuthorities()));

        if(auth.getAuthorities().contains("ROLE_TEACHER")){
            newExamResultDTO.setChangedByTeacher(new Date());
        }

        ExamResultDTO examResultDTO = examResultService.saveNewExamResult(newExamResultDTO);
        if(examResultDTO != null) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(null);
        }
        else{
            return ResponseEntity.
                    notFound()
                    .build();
        }
    }


    @GetMapping("/examSets")
    public List<ExamResultDTO> getAllExamResultsByExamSet(@RequestParam Integer examSetId) {
        return examResultService.getExamResultsByExamSet(examSetId);
    }
    @GetMapping("/examSetsAndUser")
    public List<ExamResultDTO> getAllExamSetResultsByUserAndExamSet(@RequestParam Integer examSetId, @RequestParam Integer userId) {
        return examResultService.getExamResultsByExamSetAndUser(examSetId, userId);
    }
    @GetMapping("/examSetsAndUsersAndQuestion")
    public ResponseEntity<ExamResultDTO> getAllExamSetResultsByExamSetsAndUsersAndQuestion(@RequestParam Integer examSetId, @RequestParam Integer userId, @RequestParam Integer questionId) {
        ExamResultDTO examResultDTO = examResultService.getExamResultsByExamSetAndUserIdAndQuestionId(examSetId, userId, questionId);
        if(examResultDTO != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(examResultDTO);
        }
        else{
            return ResponseEntity.
                    notFound()
                    .build();
        }
    }


}
