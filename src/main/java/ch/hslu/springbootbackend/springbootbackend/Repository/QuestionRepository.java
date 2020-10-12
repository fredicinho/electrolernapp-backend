package ch.hslu.springbootbackend.springbootbackend.Repository;

import ch.hslu.springbootbackend.springbootbackend.Entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    List<Question> findByQuestionphrase(String questionphrase);
}
