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
import ch.hslu.springbootbackend.springbootbackend.controllers.QuestionController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    private final Logger LOG = LoggerFactory.getLogger(QuestionController.class);



    private boolean ressourceExists = false;


    public QuestionDTO createNewQuestion(QuestionDTO questionDTO){

        List<Question> questions = questionRepository.findByQuestionphrase(questionDTO.getQuestionphrase());
        if (!questions.isEmpty()) {
            ressourceExists = true;
            return dtoParserQuestion.generateDTOFromObject(questions.get(0).getId());
        }
        Question question = dtoParserQuestion.generateObjectFromDTO(questionDTO);
        question.setPossibleAnswers(this.checkIfAnswersExistsInDatabase(question.getPossibleAnswers()));
        question.setCorrectAnswers(this.checkIfAnswersExistsInDatabase(question.getCorrectAnswers()));
        return (QuestionDTO) dtoParserQuestion.generateDTOFromObject(questionRepository.save(question).getId());
    }

    public List<QuestionDTO> getAllQuestions(){
        return (List<QuestionDTO>) dtoParserQuestion.generateDTOsFromObjects(questionRepository.findAll());
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
        return (List<QuestionDTO>)dtoParserQuestion.generateDTOsFromObjects(questions);
    }

    public List<QuestionDTO> getByExamSet(Integer examSetId){
        //return questionRepository.findAll();//questionRepository.find(categorySetId);
        Optional<ExamSet> examSetOptional = examSetRepository.findById(examSetId);
        List<Question> questions = new ArrayList<>();
        if(examSetOptional.isPresent()){
            ExamSet examSet = examSetOptional.get();
            questions = questionRepository.findQuestionByExamSets(examSet);
        }
        return (List<QuestionDTO>)dtoParserQuestion.generateDTOsFromObjects(questions);
    }

    public List<QuestionDTO> getByUserId(Long userId) throws ResourceNotFoundException {
        //return questionRepository.findAll();//questionRepository.find(categorySetId);
        Optional<User> userOptional = userRepository.findById(userId);
        List<Question> questions = new ArrayList<>();
        if(userOptional.isPresent()){
            User user = userOptional.get();
            questions = questionRepository.findQuestionByCreatedByUser(user);
        }
        return (List<QuestionDTO>)dtoParserQuestion.generateDTOsFromObjects(questions);
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

    public boolean ressourceExists() {
        return ressourceExists;
    }


    public void setRessourceExists(boolean ressourceExists) {
        this.ressourceExists = ressourceExists;
    }


}
