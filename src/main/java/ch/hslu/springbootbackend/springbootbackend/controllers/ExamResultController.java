package ch.hslu.springbootbackend.springbootbackend.controllers;

import ch.hslu.springbootbackend.springbootbackend.DTO.ExamResultDTO;
import ch.hslu.springbootbackend.springbootbackend.Service.EntityService.ExamResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/examResults")
public class ExamResultController {

    @Autowired
    ExamResultService examResultService;


    @PostMapping("/check")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ExamResultDTO> check(@RequestBody ExamResultDTO newExamResultDTO) {
        //String username = auth.();
        ExamResultDTO examResultDTO = examResultService.saveNewExamResult(newExamResultDTO);
        if(examResultDTO != null) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(examResultDTO);
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
