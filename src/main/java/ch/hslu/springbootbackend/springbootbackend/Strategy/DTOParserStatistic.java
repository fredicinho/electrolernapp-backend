package ch.hslu.springbootbackend.springbootbackend.Strategy;

import ch.hslu.springbootbackend.springbootbackend.DTO.StatisticDTO;
import ch.hslu.springbootbackend.springbootbackend.DTO.StatisticEvaluationDTO;
import ch.hslu.springbootbackend.springbootbackend.Entity.Question;
import ch.hslu.springbootbackend.springbootbackend.Entity.Sets.CategorySet;
import ch.hslu.springbootbackend.springbootbackend.Entity.Statistic;
import ch.hslu.springbootbackend.springbootbackend.Entity.User;
import ch.hslu.springbootbackend.springbootbackend.Repository.QuestionRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.StatisticRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.UserRepository;
import ch.hslu.springbootbackend.springbootbackend.controllers.QuestionController;
import ch.hslu.springbootbackend.springbootbackend.controllers.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class DTOParserStatistic implements DTOParserStrategy{

    @Autowired
    StatisticRepository statisticRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    public StatisticDTO generateDTOFromObject(int id) {
        Statistic statistic = statisticRepository.findById(id).orElseThrow();
        StatisticDTO statisticDTO = new StatisticDTO(statistic.getStatisticId(), statistic.getDate(), statistic.getPointsAchieved(), statistic.isMarked());

        statisticDTO.add(linkTo(methodOn(UserController.class).getUserById(statistic.getUser().getId())).withRel("user"));
        statisticDTO.add(linkTo(methodOn(QuestionController.class).getQuestionById(statistic.getQuestion().getId())).withRel("question"));
        return statisticDTO;
    }

    @Override
    public Statistic generateObjectFromDTO(Object objectDTO) {
        StatisticDTO statisticDTO = (StatisticDTO) objectDTO;
        Statistic statistic = new Statistic(
                statisticDTO.getDate(),
                statisticDTO.getPointsAchieved(),
                statisticDTO.isMarked());
        statistic.setQuestion(getQuestionFromDatabase(statisticDTO.getQuestionId()));
        statistic.setUser(getUserFromDatabase(statisticDTO.getUsername()));
        return statistic;
    }

    @Override
    public List<StatisticDTO> generateDTOsFromObjects(List list) {
        List<StatisticDTO> statisticDTOS = new ArrayList<>();
        for(Object object:list){
            Statistic statistic = (Statistic) object;
            statisticDTOS.add(generateDTOFromObject(statistic.getStatisticId()));
        }
        return statisticDTOS;
    }

    public List<StatisticEvaluationDTO> generateEvaluationDTOFromStatistic(List<Statistic> statistics) {
        List<StatisticEvaluationDTO> statisticEvaluationDTOS = new ArrayList<>();
        for (Statistic statistic: statistics
             ) {
            List<CategorySet> categorySets = statistic.getQuestion().getCategorySet();
            StatisticEvaluationDTO statisticEvaluationDTO = new StatisticEvaluationDTO(statistic.getStatisticId(),
                    statistic.getDate(), statistic.getPointsAchieved(), statistic.isMarked(),
                    categorySets.get(0).getCategory().getId(),categorySets.get(0).getCategorySetId(), statistic.getQuestion().getId());
            statisticEvaluationDTOS.add(statisticEvaluationDTO);
        }
        return statisticEvaluationDTOS;
    }

    private Question getQuestionFromDatabase(int questionId){
        Optional<Question> questionOptional = questionRepository.findById(questionId);
        Question question = null;
        if(questionOptional.isPresent()){
            question = questionOptional.get();
        }
        return question;

    }

    private User getUserFromDatabase(String username){
        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = null;
        if(userOptional.isPresent()){
            user = userOptional.get();
        }
        return user;

    }
}
