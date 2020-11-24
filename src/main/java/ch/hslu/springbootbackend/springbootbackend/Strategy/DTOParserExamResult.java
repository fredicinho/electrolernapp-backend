package ch.hslu.springbootbackend.springbootbackend.Strategy;

import ch.hslu.springbootbackend.springbootbackend.DTO.ExamResultDTO;
import ch.hslu.springbootbackend.springbootbackend.Entity.Answer;
import ch.hslu.springbootbackend.springbootbackend.Entity.ExamResult;
import ch.hslu.springbootbackend.springbootbackend.Entity.Question;
import ch.hslu.springbootbackend.springbootbackend.Entity.Sets.ExamSet;
import ch.hslu.springbootbackend.springbootbackend.Entity.User;
import ch.hslu.springbootbackend.springbootbackend.Repository.*;
import ch.hslu.springbootbackend.springbootbackend.controllers.AnswerController;
import ch.hslu.springbootbackend.springbootbackend.controllers.ExamSetController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@Service
public class DTOParserExamResult implements DTOParserStrategy {

    @Autowired
    ExamResultRepository examResultRepository;
    @Autowired
    ExamSetRepository examSetRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Override
    public ExamResultDTO generateDTOFromObject(int id) {
        ExamResult examResult = examResultRepository.findById(id).orElseThrow();
        ExamResultDTO examResultDTO = new ExamResultDTO(id);
        examResultDTO.setPointsAchieved(examResult.getPointsAchieved());
        //examResultDTO.add(linkTo(methodOn(UserController.class).getUserById(examResult.getUser().getId())).withRel("user"));
        examResultDTO.setQuestionId(examResult.getQuestion().getId());
        examResultDTO.setUserId(examResult.getUser().getId());
        examResultDTO.setChangedByTeacher(examResult.getChangedByTeacher());
        //examResultDTO.add(linkTo(methodOn(QuestionController.class).getQuestionById(examResult.getQuestion().getId())).withRel("question"));
        examResultDTO.add(linkTo(methodOn(ExamSetController.class).getExamSetById(examResult.getExamSet().getExamSetId())).withRel("examSet"));
        examResultDTO.add(linkTo(methodOn(AnswerController.class).getAnswersByExamAnswer(examResult.getId())).withRel("answersGiven"));
        return examResultDTO;
    }

    @Override
    public ExamResult generateObjectFromDTO(Object objectDTO) {
        ExamResult examResult = null;
        ExamResultDTO examResultDTO = (ExamResultDTO) objectDTO;
        Optional<ExamResult> examResultOptional = getExistingExamResult(examResultDTO);
        if(examResultOptional.isPresent()){
            examResult = examResultOptional.get();
            examResult.setAnswersToCheck(getAnswersFromDatabase(examResultDTO.getSendedAnswers()));
        }else{
            examResult = new ExamResult(
                examResultDTO.getExamResultId(),
                this.getUserFromDatabase(examResultDTO.getUserId()),
                this.getQuestionFromDatabase(examResultDTO.getQuestionId()),
                this.getExamSetFromDatabase(examResultDTO.getExamSetId()),
                this.getAnswersFromDatabase(examResultDTO.getSendedAnswers())
            );
        }

        examResult.setPointsAchieved(this.calculateScore(examResult.getQuestion().getCorrectAnswers(), examResult.getAnswersToCheck(), examResult.getQuestion().getPointsToAchieve()));
        examResult.setChangedByTeacher(examResultDTO.getChangedByTeacher());
        return examResult;
    }

    @Override
    public List<ExamResultDTO> generateDTOsFromObjects(List list) {
        List<ExamResultDTO> examResultDTOs = new ArrayList<>();
        for(Object examResultDTO:list){
            ExamResult examResult = (ExamResult) examResultDTO;
            examResultDTOs.add(generateDTOFromObject(examResult.getId()));
        }
        return examResultDTOs;
    }

    private User getUserFromDatabase(long userId){
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isPresent()){
            return userOptional.get();
        }else{
            return null;
        }
    }

    private Question getQuestionFromDatabase(int questionId){
        Optional<Question> questionOptional = questionRepository.findById(questionId);
        if(questionOptional.isPresent()){
            return questionOptional.get();
        }else{
            return null;
        }
    }

    private ExamSet getExamSetFromDatabase(int examSetId){
        Optional<ExamSet> examSetOptional = examSetRepository.findById(examSetId);
        if(examSetOptional.isPresent()){
            return examSetOptional.get();
        }else{
            return null;
        }
    }
    private List<Answer> getAnswersFromDatabase(List<String> list){
        List<Answer> answers = new LinkedList<>();
        for(String answerPhrase :list){
            Optional<Answer> answerOptional = answerRepository.findByAnswerPhrase(answerPhrase);
            if(answerOptional.isPresent()){
                answers.add(answerOptional.get());
            }else{
                Answer answer = new Answer(answerPhrase);
                answerRepository.save(answer);
                answers.add(answer);
            }
        }
        return answers;
    }
    private Optional<ExamResult> getExistingExamResult(ExamResultDTO examResultDTO){
        return examResultRepository.findAllByUserAndExamSetAndQuestion(this.getUserFromDatabase(examResultDTO.getUserId()), this.getExamSetFromDatabase(examResultDTO.getExamSetId()), this.getQuestionFromDatabase(examResultDTO.getQuestionId()));
    }

    private double calculateScore(List<Answer> correctAnswer, List<Answer> answerToCheck, int maxPoints){
        double score = 0;
        double valuePerAnswer = maxPoints/correctAnswer.size();
        for(int i = 0; i < answerToCheck.size(); i++){
            if(correctAnswer.contains(answerToCheck.get(i))){
                score = score+valuePerAnswer;
            }
            if(!correctAnswer.contains(answerToCheck.get(i))){
                score = score - valuePerAnswer;
            }
        }
        if(score < 0){
            score = 0;
        }
        return score;
    }
}
