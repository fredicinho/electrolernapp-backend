package ch.hslu.springbootbackend.springbootbackend.controllers;

import ch.hslu.springbootbackend.springbootbackend.Entity.SchoolClass;
import ch.hslu.springbootbackend.springbootbackend.Entity.User;
import ch.hslu.springbootbackend.springbootbackend.Exception.ResourceNotFoundException;
import ch.hslu.springbootbackend.springbootbackend.Repository.SchoolClassRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.UserRepository;
import ch.hslu.springbootbackend.springbootbackend.Service.EntityService.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/users")
@PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
public class UserController {
    private final Logger LOG = LoggerFactory.getLogger(StatisticController.class);

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    SchoolClassRepository schoolClassRepository;

    @GetMapping("")
    public List<User> getAllUsers() throws ResourceNotFoundException {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(value = "id") Long userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> userOptional;
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
            userOptional = userRepository.findByUsername(auth.getName());
        }else {
            userOptional = userRepository.findById(userId);
        }
        if(userOptional.isPresent()){
            User user = userOptional.get();
            user.add(linkTo(methodOn(SchoolClassController.class).getSchoolClassesByUser(userId)).withRel("schoolClasses"));
            if(user.getProfession() != null) {
                user.add(linkTo(methodOn(ProfessionController.class).getProfessionById(user.getProfession().getId())).withRel("profession"));
            }
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
    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
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

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER') or hasRole('ROLE_USER')")
    @PutMapping("/writeIn")
    public ResponseEntity<?> addUserToSchoolClass(HttpServletRequest request) {
        String writeInCode = request.getParameter("writeInCode");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            userService.addUserToSchoolClass(writeInCode, auth.getName());
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("user added to school class");
        }else{
            return ResponseEntity
                    .badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("user could not be added to school class");
        }
    }

}
