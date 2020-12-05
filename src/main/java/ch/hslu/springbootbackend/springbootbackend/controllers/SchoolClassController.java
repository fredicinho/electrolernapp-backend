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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/schoolClasses")
@PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
public class SchoolClassController {

    private final Logger LOG = LoggerFactory.getLogger(SchoolClassController.class);

    @Autowired
    SchoolClassService schoolClassService;

    @Autowired
    UserRepository userRepository;

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
    @PostMapping("/")
    public ResponseEntity<?> newSchoolClass(@RequestBody SchoolClassDTO newSchoolClass) {
        SchoolClassDTO schoolClassDTO = schoolClassService.createNewSchoolClass(newSchoolClass);
        if(schoolClassDTO != null) {
            if(schoolClassService.getRessourceExists().get()){
                schoolClassService.getRessourceExists().set(false);
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("schoolClass already exists, use put to update");
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
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
    @PutMapping("/")
    public ResponseEntity<SchoolClassDTO> updateSchoolClass(@RequestBody SchoolClassDTO newSchoolClassDTO) {
        SchoolClassDTO schoolClassDTO = schoolClassService.updateSchoolClass(newSchoolClassDTO);
        if(schoolClassDTO != null) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(schoolClassDTO);
        }
        else{
            return ResponseEntity.
                    notFound()
                    .build();
        }
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
    @GetMapping("/user")
    public List<SchoolClassDTO> getSchoolClassesByUser(@RequestParam long userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
            return schoolClassService.getAllSchoolClassesFromUser(auth.getName());
        }else{
            return schoolClassService.getAllSchoolClassesFromUser(userId);
        }
    }

    @GetMapping("/examSet")
    public List<SchoolClassDTO> getSchoolClassesByExamSet(@RequestParam int examSetId) {
        return schoolClassService.getAllSchoolClassesByExamSet(examSetId);
    }
    @GetMapping("/institution")
    public List<SchoolClassDTO> getSchoolClassesByInstitution(@RequestParam int institutionId) {
        return schoolClassService.getAllSchoolClassesByInstitution(institutionId);
    }

    @GetMapping("/")
    public List<SchoolClassDTO> getAllSchoolClasses() {
        return schoolClassService.getAllSchoolClasses();
    }
}
