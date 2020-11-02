package ch.hslu.springbootbackend.springbootbackend.Service.EntityService;

import ch.hslu.springbootbackend.springbootbackend.DTO.QuestionDTO;
import ch.hslu.springbootbackend.springbootbackend.Entity.*;
import ch.hslu.springbootbackend.springbootbackend.Exception.ResourceNotFoundException;
import ch.hslu.springbootbackend.springbootbackend.Repository.*;
import ch.hslu.springbootbackend.springbootbackend.controllers.MediaController;
import ch.hslu.springbootbackend.springbootbackend.controllers.QuestionController;
import ch.hslu.springbootbackend.springbootbackend.controllers.StatisticController;
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

    private final Logger LOG = LoggerFactory.getLogger(QuestionController.class);



    public Question createNewQuestion(QuestionDTO newQuestion) {
        
        List<Question> questions = questionRepository.findByQuestionphrase(newQuestion.getQuestionphrase());

        if (!questions.isEmpty()) {
            System.out.println("Question already exists!!!");
            return questions.get(0);
        }

        for (int i = 0; i < newQuestion.getPossibleAnswers().size(); i++) {
            Answer answer = checkIfAnswerExists(newQuestion.getPossibleAnswers().get(i).getAnswerPhrase());
            if (answer.equals(newQuestion.getPossibleAnswers().get(i))) {
            } else {
                newQuestion.getPossibleAnswers().set(i, answer);
            }
        }


            return questionRepository.save(this.generateQuestionFromQuestionDTO(newQuestion));
    }

    public List<QuestionDTO> getAllQuestions() throws ResourceNotFoundException {
        return generateQuestionDTOFromQuestion(questionRepository.findAll());
    }

    public QuestionDTO getById(int questionId) throws ResourceNotFoundException {
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

    private Answer checkIfAnswerExists(String answerPhrase) {
        Optional<Answer> foundedAnswer = answerRepository.findByAnswerPhrase(answerPhrase);
        if (foundedAnswer.isPresent()) {
            return foundedAnswer.get();
        } else {
            return new Answer(answerPhrase);
        }
    }

    private QuestionDTO generateQuestionDTOFromQuestion(int questionId) throws ResourceNotFoundException {
        Question question = questionRepository.findById(questionId).orElseThrow(
                () -> new ResourceNotFoundException("Question not found for this id :: " + questionId ));
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
        return questionDTO;
    }
    private List<QuestionDTO> generateQuestionDTOFromQuestion(List<Question> questions) throws ResourceNotFoundException {
        List<QuestionDTO> questionDTO = new ArrayList<>();
        for(Question question:questions){
            questionDTO.add(generateQuestionDTOFromQuestion(question.getId()));
        }
        return questionDTO;
    }

    public Question generateQuestionFromQuestionDTO(QuestionDTO questionDTO){
        Question question = null;
        try {
            question = new Question(questionDTO.getQuestionphrase(), questionDTO.getPossibleAnswers(), questionDTO.getCorrectAnswers(), questionDTO.getQuestionType(), this.getCategorySets(questionDTO.getCategorySetIds()), this.getImage(questionDTO.getQuestionImageId()), this.getImage(questionDTO.getAnswerImageId()));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }return question;
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
        List<CategorySet> stat = new ArrayList<>();
        for(Integer categorySetId :categorySetIds){
            Optional<CategorySet> categorySetOptional = categorySetRepository.findById(categorySetId);
            if(categorySetOptional.isPresent()){
                stat.add(categorySetOptional.get());
            }
        }
        return stat;
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

}
