package ch.hslu.springbootbackend.springbootbackend.controllers;

import ch.hslu.springbootbackend.springbootbackend.Entity.Profession;
import ch.hslu.springbootbackend.springbootbackend.Entity.Question;
import ch.hslu.springbootbackend.springbootbackend.Entity.User;
import ch.hslu.springbootbackend.springbootbackend.Repository.ProfessionRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.QuestionRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/professions")
@PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')" )
public class ProfessionController {

    private final Logger LOG = LoggerFactory.getLogger(CategoryController.class);
    @Autowired
    ProfessionRepository professionRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    QuestionRepository questionRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Profession> getProfessionById(@PathVariable(value = "id") Integer professionId) {
        Profession profession = professionRepository.findById(professionId).orElse(null);
        if(profession != null) {
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(profession);
        }else {
            return ResponseEntity.
                    notFound()
                    .build();
        }
    }
    @GetMapping("/questions")
    public List<Profession> getProfessionByQuestion(@RequestParam(value = "id") Integer questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow();
        List<Profession> professionList= professionRepository.getAllByQuestionsInProfession(question);

        return professionList;

    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Profession> newQuestion(@Valid @RequestBody Profession professionPara) {
        Profession profession = professionRepository.save(professionPara);
        if(profession != null) {
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(profession);
        }
        else{
            return ResponseEntity.
                    notFound()
                    .build();
        }
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Profession> addUserToProfession(@Valid @PathVariable(value = "id") Integer professionId, @RequestParam long userId) {
        Profession profession = professionRepository.findById(professionId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        if(profession != null && user != null) {
            if(!profession.getUsersInProfession().contains(user)) {
                profession.getUsersInProfession().add(user);
                profession = professionRepository.save(profession);
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(profession);
            }
            else{
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .build();
            }
        }
        else{
            return ResponseEntity
                    .badRequest()
                    .build();
        }
    }

}
