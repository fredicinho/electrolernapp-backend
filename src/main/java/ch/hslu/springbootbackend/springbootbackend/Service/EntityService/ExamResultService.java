package ch.hslu.springbootbackend.springbootbackend.Service.EntityService;

import ch.hslu.springbootbackend.springbootbackend.DTO.ExamResultDTO;
import ch.hslu.springbootbackend.springbootbackend.Entity.ExamResult;
import ch.hslu.springbootbackend.springbootbackend.Repository.ExamResultRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.ExamSetRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.QuestionRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.UserRepository;
import ch.hslu.springbootbackend.springbootbackend.Strategy.DTOParserExamResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


}
