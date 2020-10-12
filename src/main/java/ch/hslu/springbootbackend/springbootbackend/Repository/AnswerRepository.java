package ch.hslu.springbootbackend.springbootbackend.Repository;

import ch.hslu.springbootbackend.springbootbackend.Entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    List<Answer> findByAnswerPhrase(String answerPhrase);
}
