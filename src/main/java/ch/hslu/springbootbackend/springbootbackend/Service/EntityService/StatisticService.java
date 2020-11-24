package ch.hslu.springbootbackend.springbootbackend.Service.EntityService;

import ch.hslu.springbootbackend.springbootbackend.DTO.StatisticDTO;
import ch.hslu.springbootbackend.springbootbackend.Entity.User;
import ch.hslu.springbootbackend.springbootbackend.Repository.QuestionRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.StatisticRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.UserRepository;
import ch.hslu.springbootbackend.springbootbackend.Strategy.DTOParserStatistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StatisticService {

    @Autowired
    StatisticRepository statisticRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DTOParserStatistic dtoParserStatistic;


    public List<StatisticDTO> getByUserId(long userId){
        return dtoParserStatistic.generateDTOsFromObjects(statisticRepository.findByUserId(userId));
    }
    public List<StatisticDTO> getByUsername(String username){
        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isPresent()) {
            return dtoParserStatistic.generateDTOsFromObjects(statisticRepository.findByUserId(userOptional.get().getId()));
        }else {
            return null;
        }
    }

    public List<StatisticDTO> getByQuestionId(int questionId){
        return dtoParserStatistic.generateDTOsFromObjects(statisticRepository.findByQuestionId(questionId));
    }
    public List<StatisticDTO> getByUserAndQuestion(int userId, int questionId){
        return dtoParserStatistic.generateDTOsFromObjects(statisticRepository.findByUserAndQuestion(userId, questionId));
    }
    public List<StatisticDTO> getByUsernameAndQuestion(String username, int questionId){
        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isPresent()) {
            return dtoParserStatistic.generateDTOsFromObjects(statisticRepository.findByUserAndQuestion(userOptional.get().getId(), questionId));
        }else {
            return null;
        }    }

    public StatisticDTO addNewStatistic(StatisticDTO statDTO){
        return dtoParserStatistic.generateDTOFromObject(statisticRepository.save(dtoParserStatistic.generateObjectFromDTO(statDTO)).getStatisticId());
    }

    public List<StatisticDTO> addNewStatistics(List<StatisticDTO> statisticDTOS){
        List<StatisticDTO> statisticDTOS1 = new ArrayList<>();
        for(StatisticDTO statisticDTO : statisticDTOS){
            statisticDTOS1.add(this.addNewStatistic(statisticDTO));
        }return statisticDTOS1;
    }

}
