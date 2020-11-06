package ch.hslu.springbootbackend.springbootbackend.Service.EntityService;

import ch.hslu.springbootbackend.springbootbackend.DTO.SchoolClassDTO;
import ch.hslu.springbootbackend.springbootbackend.Entity.SchoolClass;
import ch.hslu.springbootbackend.springbootbackend.Entity.Sets.ExamSet;
import ch.hslu.springbootbackend.springbootbackend.Entity.User;
import ch.hslu.springbootbackend.springbootbackend.Exception.ResourceNotFoundException;
import ch.hslu.springbootbackend.springbootbackend.Repository.ExamSetRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.SchoolClassRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.UserRepository;
import ch.hslu.springbootbackend.springbootbackend.controllers.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class SchoolClassService {

    @Autowired
    SchoolClassRepository schoolClassRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ExamSetRepository examSetRepository;

    private final Logger LOG = LoggerFactory.getLogger(SchoolClassService.class);

    public SchoolClassDTO createNewSchoolClass(SchoolClassDTO schoolClassDTO) throws ResourceNotFoundException {

        List<SchoolClass> schoolClasses = schoolClassRepository.findByName(schoolClassDTO.getName());
        if (!schoolClasses.isEmpty()) {
            return this.generateSchoolClassDTOFromSchoolClass(schoolClassDTO.getId());
        }
        SchoolClass schoolClass = this.generateSchoolClassFromSchoolClassDTO(schoolClassDTO);
        //question.setPossibleAnswers(this.checkIfAnswersExistsInDatabase(question.getPossibleAnswers()));
        //question.setCorrectAnswers(this.checkIfAnswersExistsInDatabase(question.getCorrectAnswers()));
        return this.generateSchoolClassDTOFromSchoolClass(schoolClassRepository.save(schoolClass).getId());
    }

    public List<SchoolClassDTO> getAllSchoolClassesFromUser(Long userId) throws ResourceNotFoundException {
        List<SchoolClassDTO> schoolClassDTOS = new LinkedList<>();
        Optional<User > userOptional = userRepository.findById(userId);
        if(userOptional.isPresent()){
            schoolClassDTOS = generateSchoolClassDTOFromSchoolClass(schoolClassRepository.findAllByUsersInClass(userOptional.get()));
        }
        return schoolClassDTOS;
    }

    private SchoolClassDTO generateSchoolClassDTOFromSchoolClass(int schoolClassId) throws ResourceNotFoundException {
        SchoolClass schoolClass = schoolClassRepository.findById(schoolClassId).orElseThrow(
                () -> new ResourceNotFoundException("School Class not found for this id :: " + schoolClassId ));
        SchoolClassDTO schoolClassDTO = new SchoolClassDTO(schoolClassId, schoolClass.getName(), schoolClass.getDescription());

        //questionDTO.add(linkTo(methodOn(AnswerController.class).getPossibleAnswersByQuestion(questionId)).withRel("possibleAnswers"));
        //questionDTO.add(linkTo(methodOn(AnswerController.class).getCorrectAnswersByQuestion(questionId)).withRel("correctAnswers"));
        schoolClassDTO.add(linkTo(methodOn(UserController.class).getUsersBySchoolClasses(schoolClassId)).withRel("usersInSchoolClass"));
        //schoolClassDTO.add(linkTo(methodOn(QuestionController.class).getQuestionsByExamSet(schoolClassId)).withRel("questionsInExamSet"));

        return schoolClassDTO;
    }
    private List<SchoolClassDTO> generateSchoolClassDTOFromSchoolClass(List<SchoolClass> schoolClasses) throws ResourceNotFoundException {
        List<SchoolClassDTO> schoolClassDTOS = new ArrayList<>();
        for(SchoolClass schoolClass:schoolClasses){
            schoolClassDTOS.add(generateSchoolClassDTOFromSchoolClass(schoolClass.getId()));
        }
        return schoolClassDTOS;
    }


    public SchoolClass generateSchoolClassFromSchoolClassDTO(SchoolClassDTO schoolClassDTO) throws ResourceNotFoundException {

        SchoolClass schoolClass = new SchoolClass(
                schoolClassDTO.getName(),
                schoolClassDTO.getDescription());
        LOG.warn(schoolClass.toString());
        schoolClass.setExamSetsForSchoolClass(getExamSetsFromDatabase(schoolClassDTO.getExamSetsForSchoolClass()));
        schoolClass.setUsersInClass(getUserFromDatabase(schoolClassDTO.getUsersInClass()));
        return schoolClass;
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

}
