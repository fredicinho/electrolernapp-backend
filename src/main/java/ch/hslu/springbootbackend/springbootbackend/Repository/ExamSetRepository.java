package ch.hslu.springbootbackend.springbootbackend.Repository;

import ch.hslu.springbootbackend.springbootbackend.Entity.SchoolClass;
import ch.hslu.springbootbackend.springbootbackend.Entity.Sets.ExamSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamSetRepository extends JpaRepository<ExamSet, Integer> {
    Optional<ExamSet> findByTitle(String title);
    List<ExamSet> findAllBySchoolClassesInExamSet(SchoolClass schoolClass);
}
