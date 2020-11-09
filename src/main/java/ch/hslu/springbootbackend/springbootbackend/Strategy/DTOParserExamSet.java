package ch.hslu.springbootbackend.springbootbackend.Strategy;

import ch.hslu.springbootbackend.springbootbackend.DTO.ExamSetDTO;
import ch.hslu.springbootbackend.springbootbackend.Entity.Question;
import ch.hslu.springbootbackend.springbootbackend.Entity.SchoolClass;
import ch.hslu.springbootbackend.springbootbackend.Entity.Sets.ExamSet;
import ch.hslu.springbootbackend.springbootbackend.Entity.User;
import ch.hslu.springbootbackend.springbootbackend.Repository.ExamSetRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.QuestionRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.SchoolClassRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.UserRepository;
import ch.hslu.springbootbackend.springbootbackend.controllers.QuestionController;
import ch.hslu.springbootbackend.springbootbackend.controllers.SchoolClassController;
import ch.hslu.springbootbackend.springbootbackend.controllers.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class DTOParserExamSet implements DTOParserStrategy{

    @Autowired
    ExamSetRepository examSetRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    SchoolClassRepository schoolClassRepository;
    @Autowired
    UserRepository userRepository;


    @Override
    public ExamSetDTO generateDTOFromObject(int id) {
        ExamSet examSet = examSetRepository.findById(id).orElseThrow();
        ExamSetDTO examSetDTO = new ExamSetDTO(examSet.getExamSetId(), examSet.getTitle(), examSet.getStartDate(), examSet.getEndDate());
        examSetDTO.add(linkTo(methodOn(QuestionController.class).getQuestionsByExamSet(examSet.getExamSetId())).withRel("questionsInExamSet"));
        examSetDTO.add(linkTo(methodOn(SchoolClassController.class).getSchoolClassesByExamSet(examSet.getExamSetId())).withRel("classesInExamSet"));
        examSetDTO.add(linkTo(methodOn(UserController.class).getUserById(examSet.getCreatedByUser().getId())).withRel("created by"));
        return examSetDTO;
    }

    @Override
    public ExamSet generateObjectFromDTO(Object objectDTO) {
        ExamSet examSet = null;
        ExamSetDTO examSetDTO = (ExamSetDTO) objectDTO;

        examSet = new ExamSet(
                examSetDTO.getTitle(),
                this.getQuestionsFromDatabase(examSetDTO.getQuestionsInExamSet()),
                this.getSchoolClassesFromDatabase(examSetDTO.getSchoolClassesInExamSet()),
                examSetDTO.getStartDate(),
                examSetDTO.getEndDate(),
                this.getUserFromDatabase(examSetDTO.getUserId())
            );

        return examSet;
    }

    @Override
    public List<ExamSetDTO> generateDTOsFromObjects(List list) {
        List<ExamSetDTO> examSetDTOS = new ArrayList<>();
        for(Object examSetDTO:list){
            ExamSetDTO examSet = (ExamSetDTO) examSetDTO;
            examSetDTOS.add(generateDTOFromObject(examSet.getExamSetId()));
        }
        return examSetDTOS;
    }


    private List<Question> getQuestionsFromDatabase(List<Integer> list){
        List<Question> questionList = new LinkedList<>();
        for(Integer questionId :list){
            Optional<Question> questionOptional = questionRepository.findById(questionId);
            if(questionOptional.isPresent()){
                questionList.add(questionOptional.get());
            }
        }
        return questionList;
    }

    private List<SchoolClass> getSchoolClassesFromDatabase(List<Integer> list){
        List<SchoolClass> schoolClasses = new LinkedList<>();
        for(Integer schoolClassId :list){
            Optional<SchoolClass> schoolClassOptional = schoolClassRepository.findById(schoolClassId);
            if(schoolClassOptional.isPresent()){
                schoolClasses.add(schoolClassOptional.get());
            }
        }
        return schoolClasses;
    }

    private User getUserFromDatabase(long userId){
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isPresent()){
            return userOptional.get();
        }else{
            return null;
        }
    }

}