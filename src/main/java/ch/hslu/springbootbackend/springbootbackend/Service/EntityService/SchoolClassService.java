package ch.hslu.springbootbackend.springbootbackend.Service.EntityService;

import ch.hslu.springbootbackend.springbootbackend.DTO.SchoolClassDTO;
import ch.hslu.springbootbackend.springbootbackend.Entity.SchoolClass;
import ch.hslu.springbootbackend.springbootbackend.Entity.User;
import ch.hslu.springbootbackend.springbootbackend.Repository.ExamSetRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.SchoolClassRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.UserRepository;
import ch.hslu.springbootbackend.springbootbackend.Strategy.DTOParserSchoolClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class SchoolClassService {

    @Autowired
    SchoolClassRepository schoolClassRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ExamSetRepository examSetRepository;

    @Autowired
    DTOParserSchoolClass dtoParserSchoolClass;

    private final Logger LOG = LoggerFactory.getLogger(SchoolClassService.class);


    private boolean ressourceExists = false;


    public SchoolClassDTO createNewSchoolClass(SchoolClassDTO schoolClassDTO){

        Optional<SchoolClass> schoolClassOptional = schoolClassRepository.findByName(schoolClassDTO.getName());
        if (schoolClassOptional.isPresent()) {
            this.ressourceExists = true;
            return dtoParserSchoolClass.generateDTOFromObject(schoolClassOptional.get().getId());
        }
        SchoolClass schoolClass = this.dtoParserSchoolClass.generateObjectFromDTO(schoolClassDTO);
        return dtoParserSchoolClass.generateDTOFromObject(schoolClassRepository.save(schoolClass).getId());
    }

    public List<SchoolClassDTO> getAllSchoolClassesFromUser(Long userId) {
        List<SchoolClassDTO> schoolClassDTOS = new LinkedList<>();
        Optional<User > userOptional = userRepository.findById(userId);
        if(userOptional.isPresent()){
            schoolClassDTOS = dtoParserSchoolClass.generateDTOsFromObjects(schoolClassRepository.findAllByUsersInClass(userOptional.get()));
        }
        return schoolClassDTOS;
    }

    public boolean ressourceExists() {
        return ressourceExists;
    }



}
