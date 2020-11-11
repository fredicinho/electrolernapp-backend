package ch.hslu.springbootbackend.springbootbackend.controllers;

import ch.hslu.springbootbackend.springbootbackend.DTO.ExamResultDTO;
import ch.hslu.springbootbackend.springbootbackend.DTO.ExamSetDTO;
import ch.hslu.springbootbackend.springbootbackend.Repository.ExamSetRepository;
import ch.hslu.springbootbackend.springbootbackend.Service.EntityService.ExamResultService;
import ch.hslu.springbootbackend.springbootbackend.Service.EntityService.ExamSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    @Autowired
    ExamResultService examResultService;

    @PostMapping("")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ExamSetDTO> newSchoolClass(@RequestBody ExamSetDTO newExamSet) {
        ExamSetDTO examSetDTO = examSetService.createNewExamSet(newExamSet);
        if(examSetDTO != null) {
            if(examSetService.ressourceExists()){
                examSetService.setRessourceExists(false);
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

    @PostMapping("/check")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ExamResultDTO> check(@RequestBody ExamResultDTO newExamResultDTO) {
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

    @GetMapping("/")
    public List<ExamSetDTO> getAllExamSets() {
        return examSetService.getAllExamSets();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamSetDTO> getExamSetById(@PathVariable(value = "id") Integer examSetId) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(examSetService.getExamSetById(examSetId));
    }

    @PutMapping("/{id}/schoolClasses")
    public ResponseEntity<ExamSetDTO> updateExamSetSchoolClassesIn(@PathVariable(value = "id") Integer examSetId, @RequestParam Integer schoolClassId) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(examSetService.updateExamSetSchoolClassesIn(examSetId, schoolClassId));
    }

    @PutMapping("/{id}/questions")
    public ResponseEntity<ExamSetDTO> updateExamSetQuestionsIn(@PathVariable(value = "id") Integer examSetId, @RequestBody List<Integer> questionIds) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(examSetService.updateExamSetQuestionsIn(examSetId, questionIds));
    }

}
