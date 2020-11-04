package ch.hslu.springbootbackend.springbootbackend.Service.EntityService;

import ch.hslu.springbootbackend.springbootbackend.DTO.QuestionDTO;
import ch.hslu.springbootbackend.springbootbackend.Entity.*;
import ch.hslu.springbootbackend.springbootbackend.Exception.ResourceNotFoundException;
import ch.hslu.springbootbackend.springbootbackend.Repository.*;
import ch.hslu.springbootbackend.springbootbackend.controllers.MediaController;
import ch.hslu.springbootbackend.springbootbackend.controllers.QuestionController;
import ch.hslu.springbootbackend.springbootbackend.controllers.StatisticController;
import ch.hslu.springbootbackend.springbootbackend.controllers.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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

    private final Logger LOG = LoggerFactory.getLogger(QuestionController.class);



    public QuestionDTO createNewQuestion(QuestionDTO questionDTO) throws ResourceNotFoundException {
        
        List<Question> questions = questionRepository.findByQuestionphrase(questionDTO.getQuestionphrase());
        LOG.warn(questionDTO.getCategorySetIds().toString());
        LOG.warn(questionDTO.getUserId().toString());
/*        if (!questions.isEmpty()) {
            return this.generateQuestionDTOFromQuestion(questions.get(0).getId());
        }*/
        Question question = this.generateQuestionFromQuestionDTO(questionDTO);
        LOG.warn("in question"+question.getCategorySet().toString());
        question.setPossibleAnswers(this.checkIfAnswersExistsInDatabase(question.getPossibleAnswers()));
        question.setCorrectAnswers(this.checkIfAnswersExistsInDatabase(question.getCorrectAnswers()));
        LOG.warn(question.toString());
        return this.generateQuestionDTOFromQuestion(questionRepository.save(question).getId());
    }

    public List<QuestionDTO> getAllQuestions() throws ResourceNotFoundException {
        return generateQuestionDTOFromQuestion(questionRepository.findAll());
    }

    public QuestionDTO getById(int questionId) throws ResourceNotFoundException {

        LOG.warn("getByIdMethod");
        return this.generateQuestionDTOFromQuestion(questionId);
    }

    public List<QuestionDTO> getByCategorySetId(Integer categorySetId) throws ResourceNotFoundException {
        //return questionRepository.findAll();//questionRepository.find(categorySetId);
        Optional<CategorySet> categorySetOptional = categorySetRepository.findById(categorySetId);
        List<Question> questions = new ArrayList<>();
        if(categorySetOptional.isPresent()){
            CategorySet categorySet = categorySetOptional.get();
            questions = questionRepository.findQuestionByCategorySet(categorySet);
        }
        return generateQuestionDTOFromQuestion(questions);
    }

    public List<QuestionDTO> getByUserId(Long userId) throws ResourceNotFoundException {
        //return questionRepository.findAll();//questionRepository.find(categorySetId);
        Optional<User> userOptional = userRepository.findById(userId);
        List<Question> questions = new ArrayList<>();
        if(userOptional.isPresent()){
            User user = userOptional.get();
            questions = questionRepository.findQuestionByCreatedByUser(user);
        }
        return generateQuestionDTOFromQuestion(questions);
    }

    private QuestionDTO generateQuestionDTOFromQuestion(int questionId) throws ResourceNotFoundException {
        Question question = questionRepository.findById(questionId).orElseThrow(
                () -> new ResourceNotFoundException("Question not found for this id :: " + questionId ));
        LOG.warn(question.toString());
        QuestionDTO questionDTO = new QuestionDTO(questionId, question.getQuestionphrase(), question.getQuestionType());
        questionDTO.setPossibleAnswers(answerRepository.findAnswersByQuestionPossibleList(question));
        questionDTO.setCorrectAnswers(answerRepository.findAnswersByQuestionCorrectList(question));

        //questionDTO.add(linkTo(methodOn(AnswerController.class).getPossibleAnswersByQuestion(questionId)).withRel("possibleAnswers"));
        //questionDTO.add(linkTo(methodOn(AnswerController.class).getCorrectAnswersByQuestion(questionId)).withRel("correctAnswers"));
        questionDTO.add(linkTo(methodOn(StatisticController.class).getStatisticByQuestionId(questionId)).withRel("statistics"));

        if(checkIfQuestionImageExists(question)){
            questionDTO.add(linkTo(methodOn(MediaController.class).getMedia(question.getQuestionImage().getId())).withRel("questionImage"));
        }
        if(checkIfAnswerImageExists(question)){
            questionDTO.add(linkTo(methodOn(MediaController.class).getMedia(question.getAnswerImage().getId())).withRel("answerImage"));
        }
        if(checkIfUserExists(question)){
            questionDTO.add(linkTo(methodOn(UserController.class).getUserById(question.getCreatedByUser().getId())).withRel("createdBy"));
        }
        return questionDTO;
    }
    private List<QuestionDTO> generateQuestionDTOFromQuestion(List<Question> questions) throws ResourceNotFoundException {
        List<QuestionDTO> questionDTO = new ArrayList<>();
        for(Question question:questions){
            questionDTO.add(generateQuestionDTOFromQuestion(question.getId()));
        }
        return questionDTO;
    }

    public Question generateQuestionFromQuestionDTO(QuestionDTO questionDTO) throws ResourceNotFoundException {
        Question question = null;
        LOG.info("userId in generate "+questionDTO.getUserId().toString());
        //LOG.info("categorySet in generate " + question.getCategorySet().toString());
        question = new Question(
                questionDTO.getQuestionphrase(),
                questionDTO.getPossibleAnswers(),
                questionDTO.getCorrectAnswers(),
                questionDTO.getQuestionType(),
                this.getUser(questionDTO.getUserId()),
                this.getCategorySets(questionDTO.getCategorySetIds()),
                this.getImage(questionDTO.getQuestionImageId()),
                this.getImage(questionDTO.getAnswerImageId()));
        return question;
    }

    private List<Statistic> getStatistics(List<Integer> statisticIds){
        List<Statistic> stat = new ArrayList<>();
        for(Integer statisticId :statisticIds){
            Optional<Statistic> statisticOptional = statisticRepository.findById(statisticId);
            if(statisticOptional.isPresent()){
                stat.add(statisticOptional.get());
            }
        }
        return stat;
    }

    private List<CategorySet> getCategorySets(List<Integer> categorySetIds){
        List<CategorySet> categorySets = new ArrayList<>();
        for(Integer categorySetId :categorySetIds){
            Optional<CategorySet> categorySetOptional = categorySetRepository.findById(categorySetId);
            if(categorySetOptional.isPresent()){
                categorySets.add(categorySetOptional.get());
            }
        }
        return categorySets;
    }

    private User getUser(long userId) throws ResourceNotFoundException {
        return userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found for this id :: " + userId ));
    }

    private Media getImage(int imageId) throws ResourceNotFoundException {
        return mediaRepository.findById(imageId).orElseThrow(
                () -> new ResourceNotFoundException("Image not found for this id :: " + imageId ));
    }
    private boolean checkIfAnswerImageExists(Question question){
        if(question.getAnswerImage() != null){
            return true;
        }else{
            return false;
        }
    }
    private boolean checkIfQuestionImageExists(Question question){
        if(question.getQuestionImage() != null){
            return true;
        }else{
            return false;
        }
    }
    private boolean checkIfUserExists(Question question){
        if(question.getCreatedByUser() != null){
            return true;
        }else{
            return false;
        }
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
    private boolean checkIfUserExists(long userId){
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isPresent()){
            return true;
        }else{
            return false;
        }
    }

}
