package ch.hslu.springbootbackend.springbootbackend.Repository;

import ch.hslu.springbootbackend.springbootbackend.Entity.CategorySet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategorySetRepository extends JpaRepository<CategorySet, Integer> {
    List<CategorySet> findByTitleAndCategorySetNumber(String title, String categorySetNumber);
}
