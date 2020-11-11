package ch.hslu.springbootbackend.springbootbackend.Repository;

import ch.hslu.springbootbackend.springbootbackend.Entity.ExamResult;
import ch.hslu.springbootbackend.springbootbackend.Entity.Question;
import ch.hslu.springbootbackend.springbootbackend.Entity.Sets.ExamSet;
import ch.hslu.springbootbackend.springbootbackend.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExamResultRepository extends JpaRepository<ExamResult, Integer> {
    Optional<ExamResult> findAllByUserAndExamSetAndQuestion(User user, ExamSet examSet, Question question);
}
