package ch.hslu.springbootbackend.springbootbackend.Strategy;

import ch.hslu.springbootbackend.springbootbackend.DTO.QuestionDTO;
import ch.hslu.springbootbackend.springbootbackend.Entity.*;
import ch.hslu.springbootbackend.springbootbackend.Entity.Sets.CategorySet;
import ch.hslu.springbootbackend.springbootbackend.Exception.ResourceNotFoundException;
import ch.hslu.springbootbackend.springbootbackend.Repository.*;
import ch.hslu.springbootbackend.springbootbackend.controllers.MediaController;
import ch.hslu.springbootbackend.springbootbackend.controllers.StatisticController;
import ch.hslu.springbootbackend.springbootbackend.controllers.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class DTOParserQuestion implements DTOParserStrategy{
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    StatisticRepository statisticRepository;
    @Autowired
    CategorySetRepository categorySetRepository;
    @Autowired
    MediaRepository mediaRepository;


    @Override
    public QuestionDTO generateDTOFromObject(int id) {
        Question question = questionRepository.findById(id).orElseThrow();
        QuestionDTO questionDTO = new QuestionDTO(id, question.getQuestionPhrase(), question.getQuestionType(), question.getPointsToAchieve());
        questionDTO.setPossibleAnswers(answerRepository.findAnswersByQuestionPossibleList(question));
        questionDTO.setCorrectAnswers(answerRepository.findAnswersByQuestionCorrectList(question));
        //questionDTO.add(linkTo(methodOn(AnswerController.class).getPossibleAnswersByQuestion(questionId)).withRel("possibleAnswers"));
        //questionDTO.add(linkTo(methodOn(AnswerController.class).getCorrectAnswersByQuestion(questionId)).withRel("correctAnswers"));
        try {
            questionDTO.add(linkTo(methodOn(StatisticController.class).getStatisticByQuestionId(id)).withRel("statistics"));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }

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

    @Override
    public Question generateObjectFromDTO(Object objectDTO) {
        Question question = null;
        QuestionDTO questionDTO = (QuestionDTO) objectDTO;
        try {
            question = new Question(
                    questionDTO.getQuestionPhrase(),
                    questionDTO.getPossibleAnswers(),
                    questionDTO.getCorrectAnswers(),
                    questionDTO.getQuestionType(),
                    this.getUser(questionDTO.getCreatedBy()),
                    this.getCategorySets(questionDTO.getCategorySetIds()),
                    this.getImage(questionDTO.getQuestionImageId()),
                    this.getImage(questionDTO.getAnswerImageId()),
                    questionDTO.getPointsToAchieve()
            );
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        return question;
    }

    @Override
    public List<QuestionDTO> generateDTOsFromObjects(List list) {
        List<QuestionDTO> questionDTOS = new ArrayList<>();
        for(Object questionDTO:list){
            Question question = (Question)questionDTO;
            questionDTOS.add(generateDTOFromObject(question.getId()));
        }
        return questionDTOS;
    }




    private User getUser(String username) throws ResourceNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("User not found for this username :: " + username ));
    }
    private boolean checkIfAnswerImageExists(Question question){
        return question.getAnswerImage() != null;
    }
    private boolean checkIfQuestionImageExists(Question question){
        return question.getQuestionImage() != null;
    }
    private boolean checkIfUserExists(Question question){
        return question.getCreatedByUser() != null;
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
    private Media getImage(int imageId) throws ResourceNotFoundException {
        return mediaRepository.findById(imageId).orElseThrow(
                () -> new ResourceNotFoundException("Image not found for this id :: " + imageId ));
    }


}
