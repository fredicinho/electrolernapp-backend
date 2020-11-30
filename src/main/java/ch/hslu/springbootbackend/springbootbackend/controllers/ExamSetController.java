package ch.hslu.springbootbackend.springbootbackend.controllers;

import ch.hslu.springbootbackend.springbootbackend.DTO.ExamSetDTO;
import ch.hslu.springbootbackend.springbootbackend.Repository.ExamSetRepository;
import ch.hslu.springbootbackend.springbootbackend.Service.EntityService.ExamSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = "http://localhost:80", maxAge = 3600)
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/examSets")
public class ExamSetController {

    @Autowired
    ExamSetRepository examSetRepository;
    @Autowired
    ExamSetService examSetService;

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')" )
    public ResponseEntity<ExamSetDTO> newSchoolClass(@RequestBody ExamSetDTO newExamSet) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        newExamSet.setCreatedBy(auth.getName());
        ExamSetDTO examSetDTO = examSetService.createNewExamSet(newExamSet);
        if(examSetDTO != null) {
            if(examSetService.ressourceExists().get()){
                examSetService.ressourceExists().set(false);
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(examSetDTO);
            }else
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(examSetDTO);
        }
        else{
            return ResponseEntity.
                    notFound()
                    .build();
        }
    }


    @GetMapping("/")
    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public List<ExamSetDTO> getAllExamSets() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
            return examSetService.getExamSetsByUser(auth.getName());
        }else {
            return examSetService.getAllExamSets();
        }
    }

    // Just Users who are in schoolclass of exam are allowed to get the specific exam!!!
    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_EXAM')")
    @GetMapping("/{id}")
    public ResponseEntity<ExamSetDTO> getExamSetById(@PathVariable(value = "id") Integer examSetId) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(examSetService.getExamSetById(examSetId));
    }
    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}/schoolClasses")
    public ResponseEntity<ExamSetDTO> updateExamSetSchoolClassesIn(@PathVariable(value = "id") Integer examSetId, @RequestParam Integer schoolClassId) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(examSetService.updateExamSetSchoolClassesIn(examSetId, schoolClassId));
    }

    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}/questions")
    public ResponseEntity<ExamSetDTO> updateExamSetQuestionsIn(@PathVariable(value = "id") Integer examSetId, @RequestBody List<Integer> questionIds) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(examSetService.updateExamSetQuestionsIn(examSetId, questionIds));
    }

}
