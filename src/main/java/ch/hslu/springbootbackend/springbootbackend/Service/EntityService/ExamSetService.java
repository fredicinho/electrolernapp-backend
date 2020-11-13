package ch.hslu.springbootbackend.springbootbackend.Service.EntityService;

import ch.hslu.springbootbackend.springbootbackend.DTO.ExamSetDTO;
import ch.hslu.springbootbackend.springbootbackend.Entity.Question;
import ch.hslu.springbootbackend.springbootbackend.Entity.SchoolClass;
import ch.hslu.springbootbackend.springbootbackend.Entity.Sets.ExamSet;
import ch.hslu.springbootbackend.springbootbackend.Repository.ExamSetRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.QuestionRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.SchoolClassRepository;
import ch.hslu.springbootbackend.springbootbackend.Strategy.DTOParserExamSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                if(!examSetRepository.findAllBySchoolClassesInExamSet(schoolClassOptional.get()).isPresent()) {
                    examSet.getSchoolClassesInExamSet().add(schoolClassOptional.get());
                    examSetRepository.save(examSet);
                }
            }
        }
        return dtoParserExamSet.generateDTOFromObject(examSetId);
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


    public AtomicBoolean ressourceExists() {
        return ressourceExists;
    }

    public void setRessourceExists(AtomicBoolean ressourceExists) {
        this.ressourceExists = ressourceExists;
    }
}
