package ch.hslu.springbootbackend.springbootbackend.Service.EntityService;

import ch.hslu.springbootbackend.springbootbackend.DTO.ExamSetDTO;
import ch.hslu.springbootbackend.springbootbackend.Entity.Question;
import ch.hslu.springbootbackend.springbootbackend.Entity.SchoolClass;
import ch.hslu.springbootbackend.springbootbackend.Entity.Sets.ExamSet;
import ch.hslu.springbootbackend.springbootbackend.Entity.User;
import ch.hslu.springbootbackend.springbootbackend.Repository.ExamSetRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.QuestionRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.SchoolClassRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.UserRepository;
import ch.hslu.springbootbackend.springbootbackend.Strategy.DTOParserExamSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class ExamSetService {

    @Autowired
    ExamSetRepository examSetRepository;
    @Autowired
    SchoolClassRepository schoolClassRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    DTOParserExamSet dtoParserExamSet;
    @Autowired
    UserRepository userRepository;


    private AtomicBoolean ressourceExists = new AtomicBoolean();

    public ExamSetDTO createNewExamSet(ExamSetDTO examSetDTO) {
        Optional<ExamSet> examSetOptional = examSetRepository.findByTitle(examSetDTO.getTitle());
        if (examSetOptional.isPresent()) {
            ressourceExists.set(true);
            return dtoParserExamSet.generateDTOFromObject(examSetOptional.get().getExamSetId());
        }
        ExamSet examSet = dtoParserExamSet.generateObjectFromDTO(examSetDTO);
        return dtoParserExamSet.generateDTOFromObject(examSetRepository.save(examSet).getExamSetId());
    }

    public ExamSetDTO getExamSetById(int examSet){
        return dtoParserExamSet.generateDTOFromObject(examSetRepository.getOne(examSet).getExamSetId());
    }

    public List<ExamSetDTO> getAllExamSets(){
        return dtoParserExamSet.generateDTOsFromObjects(examSetRepository.findAll());
    }

    public ExamSetDTO updateExamSetSchoolClassesIn(int examSetId, int schoolClassId){
        Optional<ExamSet> examSetOptional = examSetRepository.findById(examSetId);
        if(examSetOptional.isPresent()){
            Optional<SchoolClass> schoolClassOptional = schoolClassRepository.findById(schoolClassId);
            if(schoolClassOptional.isPresent()){
                ExamSet examSet = examSetOptional.get();
                if(examSetRepository.findAllBySchoolClassesInExamSet(schoolClassOptional.get()).size() != 0) {
                    examSet.getSchoolClassesInExamSet().add(schoolClassOptional.get());
                    examSetRepository.save(examSet);
                }
            }
        }
        return dtoParserExamSet.generateDTOFromObject(examSetId);
    }

    public long getTimeForExam(int examSetId){
        ExamSet examSet = examSetRepository.findById(examSetId).orElseThrow(() -> new RuntimeException("Error: Exam is not found."));;
        return examSet.getEndDate().getTime()-examSet.getStartDate().getTime();
    }

    public ExamSetDTO updateExamSetQuestionsIn(int examSetId, List<Integer> questionIds){
        Optional<ExamSet> examSetOptional = examSetRepository.findById(examSetId);
        if(examSetOptional.isPresent()){
            List<Question> questionList = examSetOptional.get().getQuestionsInExamSet();
            for(Integer questionId: questionIds){
                Optional<Question> questionOptional = questionRepository.findById(questionId);
                if(questionOptional.isPresent()) questionList.add(questionOptional.get());
            }
            examSetRepository.save(examSetOptional.get());
        }
        return dtoParserExamSet.generateDTOFromObject(examSetId);
    }

    public List<ExamSetDTO> getExamSetsByUser(String username){
        List<ExamSetDTO> examSetDTOS = new ArrayList<>();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Error: User is not found."));
        List<SchoolClass> schoolClasses = user.getInSchoolClasses();
        for(SchoolClass schoolClass : schoolClasses){
            examSetDTOS.addAll(dtoParserExamSet.generateDTOsFromObjects(examSetRepository.findAllBySchoolClassesInExamSet(schoolClass)));
        }
        return examSetDTOS;
    }

    public AtomicBoolean ressourceExists() {
        return ressourceExists;
    }

    public void setRessourceExists(AtomicBoolean ressourceExists) {
        this.ressourceExists = ressourceExists;
    }
}
