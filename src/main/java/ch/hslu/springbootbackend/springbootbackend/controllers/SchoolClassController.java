package ch.hslu.springbootbackend.springbootbackend.controllers;

import ch.hslu.springbootbackend.springbootbackend.DTO.SchoolClassDTO;
import ch.hslu.springbootbackend.springbootbackend.Repository.UserRepository;
import ch.hslu.springbootbackend.springbootbackend.Service.EntityService.SchoolClassService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/schoolClasses")
public class SchoolClassController {

    private final Logger LOG = LoggerFactory.getLogger(SchoolClassController.class);

    @Autowired
    SchoolClassService schoolClassService;

    @Autowired
    UserRepository userRepository;

    @PostMapping("")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<SchoolClassDTO> newSchoolClass(@RequestBody SchoolClassDTO newSchoolClass) {
            SchoolClassDTO schoolClassDTO = schoolClassService.createNewSchoolClass(newSchoolClass);
        if(schoolClassDTO != null) {
            if(schoolClassService.ressourceExists()){
                schoolClassService.setRessourceExists(false);
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(schoolClassDTO);
            }else
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(schoolClassDTO);
        }
        else{
            return ResponseEntity.
                    notFound()
                    .build();
        }
    }

    @GetMapping("/user")
    public List<SchoolClassDTO> getSchoolClassesByUser(@RequestParam long userId) {
        return schoolClassService.getAllSchoolClassesFromUser(userId);
    }

    @GetMapping("/examSet")
    public List<SchoolClassDTO> getSchoolClassesByExamSet(@RequestParam int examSetId) {
        return schoolClassService.getAllSchoolClassesByExamSet(examSetId);


    }

    @GetMapping("/")
    public List<SchoolClassDTO> getAllSchoolClasses() {
        return schoolClassService.getAllSchoolClasses();
    }
}
