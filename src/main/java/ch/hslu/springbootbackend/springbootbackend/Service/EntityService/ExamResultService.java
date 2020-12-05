package ch.hslu.springbootbackend.springbootbackend.Service.EntityService;

import ch.hslu.springbootbackend.springbootbackend.DTO.ExamResultDTO;
import ch.hslu.springbootbackend.springbootbackend.DTO.ExamResultOverviewDTO;
import ch.hslu.springbootbackend.springbootbackend.Entity.ExamResult;
import ch.hslu.springbootbackend.springbootbackend.Entity.Question;
import ch.hslu.springbootbackend.springbootbackend.Entity.SchoolClass;
import ch.hslu.springbootbackend.springbootbackend.Entity.Sets.ExamSet;
import ch.hslu.springbootbackend.springbootbackend.Entity.User;
import ch.hslu.springbootbackend.springbootbackend.Exception.ResourceNotFoundException;
import ch.hslu.springbootbackend.springbootbackend.Repository.*;
import ch.hslu.springbootbackend.springbootbackend.Strategy.DTOParserExamResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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
    @Autowired
    SchoolClassRepository schoolClassRepository;

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

    public List<ExamResultDTO> getAll(){
        return dtoParserExamResult.generateDTOsFromObjects(examResultRepository.findAll());
    }

    public List<ExamResultDTO> getExamResultsByExamSetAndUser(int examSetId, String username){
        List<ExamResultDTO>  examResultDTO = new ArrayList<>();
        Optional<ExamSet> examSetOptional = examSetRepository.findById(examSetId);
        Optional<User> userOptional = userRepository.findByUsername(username);
        if(examSetOptional.isPresent() && userOptional.isPresent()){
            List<ExamResult> examResultOptional = examResultRepository.findAllByExamSetAndUser(examSetOptional.get(), userOptional.get());
            examResultDTO = dtoParserExamResult.generateDTOsFromObjects(examResultOptional);
        }
        return examResultDTO;

    }

    public List<ExamResultDTO> getExamResultsByExamSetAndSchoolClass(int examSetId, int schoolClassId) throws ResourceNotFoundException {
        List<ExamResult> examResults = new ArrayList<>();
        Optional<SchoolClass> schoolClass = schoolClassRepository.findById(schoolClassId);
        if (schoolClass.isPresent()) {
            List<User> allUsers = userRepository.findAllByInSchoolClasses(schoolClass.get());
            Optional<ExamSet> examSetOptional = examSetRepository.findById(examSetId);
            if (examSetOptional.isPresent()) {
                for (User user : allUsers) {
                    examResults.addAll(examResultRepository.findAllByExamSetAndUser(examSetOptional.get(), user));
                }
                return dtoParserExamResult.generateDTOsFromObjects(examResults);
            } else {
                throw new ResourceNotFoundException("Examset with the id ::" + examSetId + " couldn't be founded.");
            }
        } else {
            throw new ResourceNotFoundException("Schoolclass with the id :: " + schoolClassId + " couldn't be founded.");
        }

    }

    public ExamResultDTO getExamResultsByExamSetAndUserIdAndQuestionId(int examSetId, long userId, int questionId) throws ResourceNotFoundException {
        ExamResultDTO  examResultDTO = null;
        Optional<ExamSet> examSetOptional = examSetRepository.findById(examSetId);
        if (!examSetOptional.isPresent()) {
            throw new ResourceNotFoundException("The Exam with the examSetId :: " + examSetId + " doesn't exists!");
        }
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new ResourceNotFoundException("The User with the userId :: " + userId + " doesn't exists!");
        }
        Optional<Question> questionOptional = questionRepository.findById(questionId);
        if (!questionOptional.isPresent()) {
            throw new ResourceNotFoundException("The Question with the questionId :: " + questionId + " doesn't exists!");
        }
        Optional<ExamResult> examResultOptional = examResultRepository.findAllByUserAndExamSetAndQuestion(userOptional.get(), examSetOptional.get(), questionOptional.get());
        if(examResultOptional.isPresent()) {
            return dtoParserExamResult.generateDTOFromObject(examResultOptional.get().getId());
        } else {
            ExamResultDTO examResultDto = new ExamResultDTO();
            examResultDto.setExamSetId(examSetId);
            examResultDto.setQuestionId(questionId);
            examResultDto.setUsername(userOptional.get().getUsername());
            return examResultDto;
        }

    }
    public ExamResultDTO updateExamResultPointsAchieved(int examSetId, long userId, int questionId, double pointsAchieved) throws ResourceNotFoundException {
        ExamResultDTO examResultDTO = this.getExamResultsByExamSetAndUserIdAndQuestionId(examSetId, userId, questionId);
        ExamResult examResult = dtoParserExamResult.generateObjectFromDTO(examResultDTO);
        examResult.setPointsAchieved(pointsAchieved);
        examResult.setChangedByTeacher(new Date());
        examResult.setUser(this.userRepository.getOne(userId));
        examResult.setExamSet(this.examSetRepository.getOne(examSetId));
        return dtoParserExamResult.generateDTOFromObject(examResultRepository.save(examResult).getId());

    }
    public ExamResultOverviewDTO getExamResultOverviewByExamSetAndUser(int examSetId, String username){
        ExamResultOverviewDTO examResultOverviewDTO = new ExamResultOverviewDTO(examSetId, username);
        ExamSet examSet = examSetRepository.findById(examSetId).orElseThrow(() -> new RuntimeException("ExamSet not found"));
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        List<ExamResult> examResultList = examResultRepository.findAllByExamSetAndUser(examSet, user);
        examResultOverviewDTO.setMaximalNumberOfPoints(calculateMaximalScore(examSet));
        examResultOverviewDTO.setAchievedPoints(calculateExamSetScore(examResultList));
        examResultOverviewDTO.setGrade(calculateGrade(examResultOverviewDTO));
        return examResultOverviewDTO;
    }

    private double calculateExamSetScore(List<ExamResult> list){
        double examResult = 0;
        for(ExamResult examResult1 : list){
            examResult = examResult + examResult1.getPointsAchieved();
        }
        return examResult;
    }

    private double calculateMaximalScore(ExamSet list){
        double maximalPoints = 0;
        for(Question question : list.getQuestionsInExamSet()){
            maximalPoints = maximalPoints + question.getPointsToAchieve();
        }
        return maximalPoints;
    }

    private double calculateGrade(ExamResultOverviewDTO examResultOverviewDTO){
        double pointsAchieved = examResultOverviewDTO.getAchievedPoints();
        double maximalPoints = examResultOverviewDTO.getMaximalNumberOfPoints();
        return ((pointsAchieved*5)/maximalPoints)+1;
    }



}
