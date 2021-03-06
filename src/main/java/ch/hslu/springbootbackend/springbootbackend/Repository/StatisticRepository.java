package ch.hslu.springbootbackend.springbootbackend.Repository;

import ch.hslu.springbootbackend.springbootbackend.Entity.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatisticRepository extends JpaRepository<Statistic, Integer> {

    List<Statistic> findByUserId(long userId);
    List<Statistic> findByQuestionId(int questionId);
    List<Statistic> findByUserAndQuestion(long userId, int questionId);
}
