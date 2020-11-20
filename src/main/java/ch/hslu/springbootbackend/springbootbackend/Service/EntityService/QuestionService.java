package ch.hslu.springbootbackend.springbootbackend.Service.EntityService;

import ch.hslu.springbootbackend.springbootbackend.DTO.QuestionDTO;
import ch.hslu.springbootbackend.springbootbackend.Entity.Answer;
import ch.hslu.springbootbackend.springbootbackend.Entity.Question;
import ch.hslu.springbootbackend.springbootbackend.Entity.Sets.CategorySet;
import ch.hslu.springbootbackend.springbootbackend.Entity.Sets.ExamSet;
import ch.hslu.springbootbackend.springbootbackend.Entity.User;
import ch.hslu.springbootbackend.springbootbackend.Exception.ResourceNotFoundException;
import ch.hslu.springbootbackend.springbootbackend.Repository.*;
import ch.hslu.springbootbackend.springbootbackend.Strategy.DTOParserQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class QuestionService {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    CategorySetRepository categorySetRepository;

    @Autowired
    StatisticRepository statisticRepository;

    @Autowired
    MediaRepository mediaRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ExamSetRepository examSetRepository;

    @Autowired
    DTOParserQuestion dtoParserQuestion;



    private AtomicBoolean ressourceExists = new AtomicBoolean();


    public QuestionDTO createNewQuestion(QuestionDTO questionDTO){

        List<Question> questions = questionRepository.findByQuestionPhrase(questionDTO.getQuestionPhrase());
        if (questions.size() != 0) {
            ressourceExists.set(true);
            return dtoParserQuestion.generateDTOFromObject(questions.get(0).getId());
        }
        Question question = dtoParserQuestion.generateObjectFromDTO(questionDTO);
        question.setPossibleAnswers(this.checkIfAnswersExistsInDatabase(question.getPossibleAnswers()));
        question.setCorrectAnswers(this.checkIfAnswersExistsInDatabase(question.getCorrectAnswers()));
        return dtoParserQuestion.generateDTOFromObject(questionRepository.save(question).getId());
    }

    public List<QuestionDTO> getAllQuestions(){
        return dtoParserQuestion.generateDTOsFromObjects(questionRepository.findAll());
    }

    public QuestionDTO getById(int questionId){
        return dtoParserQuestion.generateDTOFromObject(questionId);
    }

    public List<QuestionDTO> getByCategorySetId(Integer categorySetId) throws ResourceNotFoundException {
        //return questionRepository.findAll();//questionRepository.find(categorySetId);
        Optional<CategorySet> categorySetOptional = categorySetRepository.findById(categorySetId);
        List<Question> questions = new ArrayList<>();
        if(categorySetOptional.isPresent()){
            CategorySet categorySet = categorySetOptional.get();
            questions = questionRepository.findQuestionByCategorySet(categorySet);
        }
        return dtoParserQuestion.generateDTOsFromObjects(questions);
    }

    public List<QuestionDTO> getByExamSet(Integer examSetId){
        Optional<ExamSet> examSetOptional = examSetRepository.findById(examSetId);
        List<Question> questions = new ArrayList<>();
        if(examSetOptional.isPresent()){
            ExamSet examSet = examSetOptional.get();
            questions = questionRepository.findQuestionByExamSets(examSet);
        }

        List<QuestionDTO> questionDTOS = dtoParserQuestion.generateDTOsFromObjects(questions);
        for(QuestionDTO questionDTO : questionDTOS){
            questionDTO.setCorrectAnswers(null);
        }
        return questionDTOS;
    }

    public List<QuestionDTO> getByUserId(Long userId) throws ResourceNotFoundException {
        //return questionRepository.findAll();//questionRepository.find(categorySetId);
        Optional<User> userOptional = userRepository.findById(userId);
        List<Question> questions = new ArrayList<>();
        if(userOptional.isPresent()){
            User user = userOptional.get();
            questions = questionRepository.findQuestionByCreatedByUser(user);
        }
        return dtoParserQuestion.generateDTOsFromObjects(questions);
    }

    private List<Answer> checkIfAnswersExistsInDatabase(List<Answer> answerList){
        List<Answer> list = new ArrayList<>();
        for(Answer answer:answerList){
            Optional<Answer> newAnswer = answerRepository.findByAnswerPhrase(answer.getAnswerPhrase());
            if(newAnswer.isPresent()){
                answer = newAnswer.get();
                answerRepository.save(answer);
                list.add(answer);
            }else {
                answer = new Answer(answer.getAnswerPhrase());
                list.add(answer);
                answerRepository.save(answer);
            }
        }
        return list;
    }

    public AtomicBoolean ressourceExists() {
        return ressourceExists;
    }


    public void setRessourceExists(AtomicBoolean ressourceExists) {
        this.ressourceExists = ressourceExists;
    }


}
