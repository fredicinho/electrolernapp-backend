package ch.hslu.springbootbackend.springbootbackend.Service.EntityService;

import ch.hslu.springbootbackend.springbootbackend.Entity.Statistic;
import ch.hslu.springbootbackend.springbootbackend.DTO.StatisticDTO;
import ch.hslu.springbootbackend.springbootbackend.Exception.ResourceNotFoundException;
import ch.hslu.springbootbackend.springbootbackend.Repository.QuestionRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.StatisticRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticService {



    @Autowired
    StatisticRepository statisticRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    public List<Statistic> getByUserId(int userId) throws ResourceNotFoundException {
        List<Statistic> statistic = statisticRepository.findByUserId(userId);

        return statistic;
    }

    public List<Statistic> getByQuestionId(int questionId) throws ResourceNotFoundException {
        List<Statistic> statistic = statisticRepository.findByQuestionId(questionId);

        return statistic;
    }
    public List<Statistic> getByUserAndQuestion(int userId, int questionId) throws ResourceNotFoundException {
        List<Statistic> statistic = statisticRepository.findByUserAndQuestion(userId, questionId);
        return statistic;
    }

    public Statistic addNewStatistic(StatisticDTO statDTO){
        System.out.println(userRepository.getOne(statDTO.getUserId()));
        System.out.println(statDTO.toString());
        Statistic stat = new Statistic(statDTO.getPointsAchieved(), statDTO.isMarked(), userRepository.getOne(statDTO.getUserId()), questionRepository.getOne(statDTO.getQuestionId()));
        return statisticRepository.save(stat);
    }

}
