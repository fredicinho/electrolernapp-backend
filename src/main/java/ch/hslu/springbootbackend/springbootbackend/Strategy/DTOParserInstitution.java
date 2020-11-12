package ch.hslu.springbootbackend.springbootbackend.Strategy;

import ch.hslu.springbootbackend.springbootbackend.DTO.InstitutionDTO;
import ch.hslu.springbootbackend.springbootbackend.Entity.Institution;
import ch.hslu.springbootbackend.springbootbackend.Entity.SchoolClass;
import ch.hslu.springbootbackend.springbootbackend.Repository.InstitutionRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.SchoolClassRepository;
import ch.hslu.springbootbackend.springbootbackend.controllers.SchoolClassController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class DTOParserInstitution implements DTOParserStrategy {
    private final Logger LOG = LoggerFactory.getLogger(ch.hslu.springbootbackend.springbootbackend.Strategy.DTOParserSchoolClass.class);
    @Autowired
    SchoolClassRepository schoolClassRepository;
    @Autowired
    InstitutionRepository institutionRepository;


    @Override
    public InstitutionDTO generateDTOFromObject(int id) {
        Institution institution = institutionRepository.findById(id).orElseThrow();
        InstitutionDTO institutionDTO = new InstitutionDTO(id, institution.getName());

        institutionDTO.add(linkTo(methodOn(SchoolClassController.class).getSchoolClassesByInstitution(id)).withRel("schoolClassesInInstitution"));

        return institutionDTO;
    }

    @Override
    public Institution generateObjectFromDTO(Object objectDTO) {
        InstitutionDTO institutionDTO = (InstitutionDTO) objectDTO;
        Institution institution = new Institution(
                institutionDTO.getName());
        institution.setSchoolClasses(getSchoolClassesFromDatabase(institutionDTO.getSchoolClassIds()));
        return institution;
    }

    @Override
    public List<InstitutionDTO> generateDTOsFromObjects(List list) {
        List<InstitutionDTO> institutionDTOS = new ArrayList<>();
        for (Object object : list) {
            Institution institution = (Institution) object;
            institutionDTOS.add(generateDTOFromObject(institution.getId()));
        }
        return institutionDTOS;
    }


    private List<SchoolClass> getSchoolClassesFromDatabase(List<Integer> list) {
        List<SchoolClass> schoolClasses = new LinkedList<>();
        for (int i = 0; i < list.size(); i++) {
            if (schoolClassRepository.findById(list.get(i)).isPresent()) {
                schoolClasses.add(schoolClassRepository.findById(list.get(i)).get());
            }
        }
        return schoolClasses;
    }


}
