package ch.hslu.springbootbackend.springbootbackend.Strategy;

import ch.hslu.springbootbackend.springbootbackend.DTO.SchoolClassDTO;
import ch.hslu.springbootbackend.springbootbackend.Entity.Institution;
import ch.hslu.springbootbackend.springbootbackend.Entity.SchoolClass;
import ch.hslu.springbootbackend.springbootbackend.Entity.Sets.ExamSet;
import ch.hslu.springbootbackend.springbootbackend.Entity.User;
import ch.hslu.springbootbackend.springbootbackend.Repository.ExamSetRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.InstitutionRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.SchoolClassRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.UserRepository;
import ch.hslu.springbootbackend.springbootbackend.controllers.InstitutionController;
import ch.hslu.springbootbackend.springbootbackend.controllers.UserController;
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
public class DTOParserSchoolClass implements DTOParserStrategy{
    private final Logger LOG = LoggerFactory.getLogger(DTOParserSchoolClass.class);
    @Autowired
    SchoolClassRepository schoolClassRepository;
    @Autowired
    ExamSetRepository examSetRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    InstitutionRepository institutionRepository;

    @Override
    public SchoolClassDTO generateDTOFromObject(int id)  {
        SchoolClass schoolClass = schoolClassRepository.findById(id).orElseThrow();
        SchoolClassDTO schoolClassDTO = new SchoolClassDTO(id, schoolClass.getName(), schoolClass.getDescription());

        schoolClassDTO.add(linkTo(methodOn(UserController.class).getUsersBySchoolClasses(id)).withRel("usersInSchoolClass"));
        schoolClassDTO.add(linkTo(methodOn(InstitutionController.class).getInstitutionById(id)).withRel("institution"));

        return schoolClassDTO;
    }

    @Override
    public SchoolClass generateObjectFromDTO(Object objectDTO) {
        SchoolClassDTO schoolClassDTO = (SchoolClassDTO) objectDTO;
        SchoolClass schoolClass = new SchoolClass(
                schoolClassDTO.getName(),
                schoolClassDTO.getDescription());
        LOG.warn(schoolClass.toString());
        schoolClass.setExamSetsForSchoolClass(getExamSetsFromDatabase(schoolClassDTO.getExamSetsForSchoolClass()));
        schoolClass.setUsersInClass(getUserFromDatabase(schoolClassDTO.getUsersInClass()));
        schoolClass.setInstitution(this.getInstitutionFromDatabase(schoolClassDTO.getInstitutionId()));
        return schoolClass;
    }

    @Override
    public List<SchoolClassDTO> generateDTOsFromObjects(List list) {
        List<SchoolClassDTO> schoolClassDTOS = new ArrayList<>();
        for(Object object:list){
            SchoolClass schoolClass = (SchoolClass)object;
            schoolClassDTOS.add(generateDTOFromObject(schoolClass.getId()));
        }
        return schoolClassDTOS;
    }

    private List<ExamSet> getExamSetsFromDatabase(List<Integer> list){
        List<ExamSet> examSetList = new LinkedList<>();
        for(int i = 0; i<list.size(); i++){
            if(examSetRepository.findById(list.get(i)).isPresent()){
                examSetList.add(examSetRepository.findById(list.get(i)).get());
            }
        }
        return examSetList;
    }
    private List<User> getUserFromDatabase(List<Long> list){
        List<User> userList = new LinkedList<>();
        for(int i = 0; i<list.size(); i++){
            if(userRepository.findById(list.get(i)).isPresent()){
                userList.add(userRepository.findById(list.get(i)).get());
            }
        }
        return userList;
    }
    private Institution getInstitutionFromDatabase(int institutionId){
        Institution institution = null;
        if(institutionRepository.findById(institutionId).isPresent()){
            institution = institutionRepository.findById(institutionId).get();
        }return institution;

    }

}
