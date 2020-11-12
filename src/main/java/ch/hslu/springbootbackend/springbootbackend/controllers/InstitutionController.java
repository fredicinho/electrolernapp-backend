package ch.hslu.springbootbackend.springbootbackend.controllers;

import ch.hslu.springbootbackend.springbootbackend.DTO.InstitutionDTO;
import ch.hslu.springbootbackend.springbootbackend.Service.EntityService.InstitutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/v1/institutions")
public class InstitutionController {

    @Autowired
    InstitutionService institutionService;


    @PostMapping("")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<InstitutionDTO> newSchoolClass(@RequestBody InstitutionDTO newInstitution) {
        InstitutionDTO institutionDTO = institutionService.createNewInstitution(newInstitution);
        if(institutionDTO != null) {
            if(institutionService.isRessourceExists()){
                institutionService.setRessourceExists(false);
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(institutionDTO);
            }else
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(institutionDTO);
        }
        else{
            return ResponseEntity.
                    notFound()
                    .build();
        }
    }

    @GetMapping("/")
    public List<InstitutionDTO> getAllInstitutions() {
        return institutionService.getAllInstitutions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstitutionDTO> getExamSetById(@PathVariable(value = "id") Integer institutionId) {

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(institutionService.getInstitutionById(institutionId));
    }
}
