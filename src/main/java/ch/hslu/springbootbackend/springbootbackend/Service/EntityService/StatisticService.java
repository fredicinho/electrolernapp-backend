package ch.hslu.springbootbackend.springbootbackend.Service.EntityService;

import ch.hslu.springbootbackend.springbootbackend.DTO.StatisticDTO;
import ch.hslu.springbootbackend.springbootbackend.Exception.ResourceNotFoundException;
import ch.hslu.springbootbackend.springbootbackend.Repository.QuestionRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.StatisticRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.UserRepository;
import ch.hslu.springbootbackend.springbootbackend.Strategy.DTOParserStatistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public List<StatisticDTO> getByQuestionId(int questionId){
        return dtoParserStatistic.generateDTOsFromObjects(statisticRepository.findByQuestionId(questionId));
    }
    public List<StatisticDTO> getByUserAndQuestion(int userId, int questionId) throws ResourceNotFoundException {
        return dtoParserStatistic.generateDTOsFromObjects(statisticRepository.findByUserAndQuestion(userId, questionId));
    }

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
