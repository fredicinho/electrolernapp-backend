package ch.hslu.springbootbackend.springbootbackend.controllers;

import ch.hslu.springbootbackend.springbootbackend.DTO.SchoolClassDTO;
import ch.hslu.springbootbackend.springbootbackend.Exception.ResourceNotFoundException;
import ch.hslu.springbootbackend.springbootbackend.Repository.UserRepository;
import ch.hslu.springbootbackend.springbootbackend.Service.EntityService.SchoolClassService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    public ResponseEntity<SchoolClassDTO> newSchoolClass(@RequestBody SchoolClassDTO schoolClassDTO) {
        try {
            SchoolClassDTO schoolClassDTO1 = schoolClassService.createNewSchoolClass(schoolClassDTO);
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(schoolClassDTO1);
        } catch (ResourceNotFoundException e) {
            LOG.warn("Resource was not found: " + e.getMessage());
            return ResponseEntity.
                    notFound()
                    .build();
        }
    }

    @GetMapping("/user")
    public List<SchoolClassDTO> getSchoolClassesByUser(@RequestParam long userId) {
        List<SchoolClassDTO> schoolClassDTOS = new ArrayList<>();
        try {
            schoolClassDTOS = schoolClassService.getAllSchoolClassesFromUser(userId);
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        return schoolClassDTOS;
    }
}
