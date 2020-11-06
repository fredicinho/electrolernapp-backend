package ch.hslu.springbootbackend.springbootbackend.controllers;

import ch.hslu.springbootbackend.springbootbackend.Entity.SchoolClass;
import ch.hslu.springbootbackend.springbootbackend.Entity.User;
import ch.hslu.springbootbackend.springbootbackend.Exception.ResourceNotFoundException;
import ch.hslu.springbootbackend.springbootbackend.Repository.SchoolClassRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final Logger LOG = LoggerFactory.getLogger(StatisticController.class);

    @Autowired
    UserRepository userRepository;
    @Autowired
    SchoolClassRepository schoolClassRepository;

    @GetMapping("")
    //@PreAuthorize("hasRole('ROLE_USER')")
    public List<User> getAllUsers() throws ResourceNotFoundException {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(value = "id") Long userId) {
        //LOG.warn(foundedQuestion.toString());


            Optional<User> userOptional = userRepository.findById(userId);
            if(userOptional.isPresent()){
                User user = userOptional.get();
                user.add(linkTo(methodOn(SchoolClassController.class).getSchoolClassesByUser(userId)).withRel("schoolClasses"));

                return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(user);
            }else{
                LOG.warn("Resource was not found: ");
                return ResponseEntity.
                        notFound()
                        .build();
            }
    }

    @GetMapping("/schoolClasses")
    public List<User> getUsersBySchoolClasses(@RequestParam Integer schoolClassId) {
        //LOG.warn(foundedQuestion.toString());
        List<User> user = new LinkedList<>();
        Optional<SchoolClass> schoolClass = schoolClassRepository.findById(schoolClassId);
        if(schoolClass.isPresent()){
            user = userRepository.findAllByInSchoolClasses(schoolClass.get());
        }
        return user;

    }

}
