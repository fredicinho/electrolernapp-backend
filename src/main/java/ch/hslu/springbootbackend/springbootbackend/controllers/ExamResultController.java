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
    @PreAuthorize("hasRole('ROLE_EXAM') or hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity check(@RequestBody ExamResultDTO newExamResultDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_TEACHER"))) {
            newExamResultDTO.setChangedByTeacher(new Date());
        }
        newExamResultDTO.setUsername(auth.getName());

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


    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/examSets")
    public List<ExamResultDTO> getAllExamResultsByExamSet(@RequestParam Integer examSetId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        LOG.warn(String.valueOf(auth.getAuthorities()));
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
            return this.getAllExamSetResultsByUserAndExamSet(examSetId, auth.getName());
        }else {
            return examResultService.getExamResultsByExamSet(examSetId);
        }
    }
    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/examSetsAndUser")
    public List<ExamResultDTO> getAllExamSetResultsByUserAndExamSet(@RequestParam Integer examSetId, @RequestParam String username) {
        return examResultService.getExamResultsByExamSetAndUser(examSetId, username);
    }
    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
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
