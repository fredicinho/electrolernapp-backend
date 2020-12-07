package ch.hslu.springbootbackend.springbootbackend.Service.EntityService;

import ch.hslu.springbootbackend.springbootbackend.DTO.SchoolClassDTO;
import ch.hslu.springbootbackend.springbootbackend.Entity.Institution;
import ch.hslu.springbootbackend.springbootbackend.Entity.SchoolClass;
import ch.hslu.springbootbackend.springbootbackend.Entity.Sets.ExamSet;
import ch.hslu.springbootbackend.springbootbackend.Entity.User;
import ch.hslu.springbootbackend.springbootbackend.Repository.ExamSetRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.InstitutionRepository;
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
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class SchoolClassService {

    @Autowired
    SchoolClassRepository schoolClassRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ExamSetRepository examSetRepository;

    @Autowired
    InstitutionRepository institutionRepository;
    @Autowired
    DTOParserSchoolClass dtoParserSchoolClass;

    private final Logger LOG = LoggerFactory.getLogger(SchoolClassService.class);



    private AtomicBoolean ressourceExists = new AtomicBoolean(false);


    public SchoolClassDTO createNewSchoolClass(SchoolClassDTO schoolClassDTO){
        Optional<SchoolClass> schoolClassOptional = schoolClassRepository.findByName(schoolClassDTO.getName());
        if (!schoolClassOptional.isPresent()) {
            SchoolClass schoolClass = dtoParserSchoolClass.generateObjectFromDTO(schoolClassDTO);
            return dtoParserSchoolClass.generateDTOFromObject(schoolClassRepository.save(schoolClass).getId());
        }else {
            this.setRessourceExists(new AtomicBoolean(true));
            return dtoParserSchoolClass.generateDTOFromObject(schoolClassOptional.get().getId());
        }
    }

    public SchoolClassDTO updateSchoolClass(SchoolClassDTO schoolClassDTO){

        Optional<SchoolClass> schoolClassOptional = schoolClassRepository.findById(schoolClassDTO.getId());
        if (schoolClassOptional.isPresent()) {
            SchoolClass schoolClass = schoolClassOptional.get();
            SchoolClass schoolClassNew = this.dtoParserSchoolClass.generateObjectFromDTO(schoolClassDTO);
            schoolClass.setInstitution(schoolClassNew.getInstitution());
            schoolClass.setDescription(schoolClassNew.getDescription());
            //schoolClass.setUsersInClass(schoolClassNew.getUsersInClass());
            schoolClass.setUsersInClass(this.updateUsersIn(schoolClassNew.getUsersInClass(), schoolClass));
            schoolClass.setName(schoolClassNew.getName());
            schoolClass.setExamSetsForSchoolClass(schoolClassNew.getExamSetsForSchoolClass());
            schoolClass = schoolClassRepository.save(schoolClass);
            SchoolClassDTO schoolClassDTO1 = dtoParserSchoolClass.generateDTOFromObject(schoolClass.getId());
            return dtoParserSchoolClass.generateDTOFromObject(schoolClass.getId());
        }else{
            return null;
        }

    }


    public List<SchoolClassDTO> getAllSchoolClasses(){
        return dtoParserSchoolClass.generateDTOsFromObjects(schoolClassRepository.findAll());
    }

    public List<SchoolClassDTO> getAllSchoolClassesFromUser(Long userId) {
        List<SchoolClassDTO> schoolClassDTOS = new LinkedList<>();
        Optional<User > userOptional = userRepository.findById(userId);
        if(userOptional.isPresent()){
            schoolClassDTOS = dtoParserSchoolClass.generateDTOsFromObjects(schoolClassRepository.findAllByUsersInClass(userOptional.get()));
        }
        return schoolClassDTOS;
    }
    public List<SchoolClassDTO> getAllSchoolClassesFromUser(String username) {
        List<SchoolClassDTO> schoolClassDTOS = new LinkedList<>();
        Optional<User > userOptional = userRepository.findByUsername(username);
        if(userOptional.isPresent()){
            schoolClassDTOS = dtoParserSchoolClass.generateDTOsFromObjects(schoolClassRepository.findAllByUsersInClass(userOptional.get()));
        }
        return schoolClassDTOS;
    }

    public List<SchoolClassDTO> getAllSchoolClassesByExamSet(int examSetId) {
        List<SchoolClassDTO> schoolClassDTOS = new LinkedList<>();
        Optional<ExamSet> examSetOptional = examSetRepository.findById(examSetId);
        if(examSetOptional.isPresent()){
            schoolClassDTOS = dtoParserSchoolClass.generateDTOsFromObjects(schoolClassRepository.findAllByExamSetsForSchoolClass(examSetOptional.get()));
        }
        return schoolClassDTOS;
    }

    public List<SchoolClassDTO> getAllSchoolClassesByInstitution(int institutionId) {
        List<SchoolClassDTO> schoolClassDTOS = new LinkedList<>();
        Optional<Institution> institutionOptional = institutionRepository.findById(institutionId);
        if(institutionOptional.isPresent()){
            schoolClassDTOS = dtoParserSchoolClass.generateDTOsFromObjects(schoolClassRepository.findAllByInstitution(institutionOptional.get()));
        }
        return schoolClassDTOS;
    }

    public SchoolClassDTO getSchoolClassById(int schoolClassId) {
        return dtoParserSchoolClass.generateDTOFromObject(schoolClassId);
    }


    public AtomicBoolean getRessourceExists() {
        return ressourceExists;
    }

    public void setRessourceExists(AtomicBoolean ressourceExists) {
        this.ressourceExists = ressourceExists;
    }

    private List<User> updateUsersIn(List<User> userNew, SchoolClass schoolClass){
        for(User user : userNew) {
            if (!user.getInSchoolClasses().contains(schoolClass)){
                user.insertSchoolClass(schoolClass);
            }
        }
        return userNew;

    }


}
