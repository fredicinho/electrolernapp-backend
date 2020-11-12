package ch.hslu.springbootbackend.springbootbackend.Service.EntityService;

import ch.hslu.springbootbackend.springbootbackend.DTO.InstitutionDTO;
import ch.hslu.springbootbackend.springbootbackend.Entity.Institution;
import ch.hslu.springbootbackend.springbootbackend.Repository.InstitutionRepository;
import ch.hslu.springbootbackend.springbootbackend.Strategy.DTOParserInstitution;
import ch.hslu.springbootbackend.springbootbackend.controllers.QuestionController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InstitutionService {
    private final Logger LOG = LoggerFactory.getLogger(QuestionController.class);
    @Autowired
    InstitutionRepository institutionRepository;

    @Autowired
    DTOParserInstitution dtoParserInstitution;

    private boolean ressourceExists = false;

    public InstitutionDTO createNewInstitution(InstitutionDTO institutionDTO){

        Optional<Institution> institutionOptional = institutionRepository.findByName(institutionDTO.getName());
        if (institutionOptional.isPresent()) {
            this.ressourceExists = true;
            return dtoParserInstitution.generateDTOFromObject(institutionOptional.get().getId());
        }
        Institution institution = this.dtoParserInstitution.generateObjectFromDTO(institutionDTO);
        return dtoParserInstitution.generateDTOFromObject(institutionRepository.save(institution).getId());
    }

    public List<InstitutionDTO> getAllInstitutions(){
        return dtoParserInstitution.generateDTOsFromObjects(institutionRepository.findAll());
    }

    public InstitutionDTO getInstitutionById(Integer institutionId) {
        return dtoParserInstitution.generateDTOFromObject(institutionId);

    }

    public boolean isRessourceExists() {
        return ressourceExists;
    }

    public void setRessourceExists(boolean ressourceExists) {
        this.ressourceExists = ressourceExists;
    }

}
