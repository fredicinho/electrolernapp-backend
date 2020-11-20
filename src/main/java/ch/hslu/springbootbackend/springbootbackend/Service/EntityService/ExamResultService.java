package ch.hslu.springbootbackend.springbootbackend.Service.EntityService;

import ch.hslu.springbootbackend.springbootbackend.DTO.ExamResultDTO;
import ch.hslu.springbootbackend.springbootbackend.Entity.ExamResult;
import ch.hslu.springbootbackend.springbootbackend.Entity.Question;
import ch.hslu.springbootbackend.springbootbackend.Entity.Sets.ExamSet;
import ch.hslu.springbootbackend.springbootbackend.Entity.User;
import ch.hslu.springbootbackend.springbootbackend.Repository.ExamResultRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.ExamSetRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.QuestionRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.UserRepository;
import ch.hslu.springbootbackend.springbootbackend.Strategy.DTOParserExamResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExamResultService {

    @Autowired
    ExamResultRepository examResultRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ExamSetRepository examSetRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    DTOParserExamResult dtoParserExamResult;

    public ExamResultDTO saveNewExamResult(ExamResultDTO examResultDTO){
        ExamResult examResult = dtoParserExamResult.generateObjectFromDTO(examResultDTO);

        return dtoParserExamResult.generateDTOFromObject(examResultRepository.save(examResult).getId());
    }

    public List<ExamResultDTO> getExamResultsByExamSet(int examSetId){
        Optional<ExamSet> examSetOptional = examSetRepository.findById(examSetId);
        List<ExamResultDTO> examResultList = new ArrayList<>();
        if(examSetOptional.isPresent()){
            examResultList = dtoParserExamResult.generateDTOsFromObjects(examResultRepository.findAllByExamSet(examSetOptional.get()));
        }
        return examResultList;

    }

    public List<ExamResultDTO> getExamResultsByExamSetAndUser(int examSetId, long userId){
        List<ExamResultDTO>  examResultDTO = new ArrayList<>();
        Optional<ExamSet> examSetOptional = examSetRepository.findById(examSetId);
        Optional<User> userOptional = userRepository.findById(userId);
        if(examSetOptional.isPresent() && userOptional.isPresent()){
            List<ExamResult> examResultOptional = examResultRepository.findAllByExamSetAndUser(examSetOptional.get(), userOptional.get());
            examResultDTO = dtoParserExamResult.generateDTOsFromObjects(examResultOptional);
        }
        return examResultDTO;

    }

    public ExamResultDTO getExamResultsByExamSetAndUserIdAndQuestionId(int examSetId, long userId, int questionId){
        ExamResultDTO  examResultDTO = null;
        Optional<ExamSet> examSetOptional = examSetRepository.findById(examSetId);
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Question> questionOptional = questionRepository.findById(questionId);
        if(examSetOptional.isPresent() && userOptional.isPresent() && questionOptional.isPresent()){
            Optional<ExamResult> examResultOptional =examResultRepository.findAllByUserAndExamSetAndQuestion(userOptional.get(), examSetOptional.get(), questionOptional.get());
            if(examResultOptional.isPresent()) {
                examResultDTO = dtoParserExamResult.generateDTOFromObject(examResultOptional.get().getId());
            }
        }
        return examResultDTO;

    }


}
