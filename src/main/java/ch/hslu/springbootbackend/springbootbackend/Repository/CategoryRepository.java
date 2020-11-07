package ch.hslu.springbootbackend.springbootbackend.Repository;

import ch.hslu.springbootbackend.springbootbackend.Entity.Category;
import ch.hslu.springbootbackend.springbootbackend.Entity.Sets.ExamSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findByName(String name);
    List<Category> findAllByExamSets(ExamSet examSet);
}
