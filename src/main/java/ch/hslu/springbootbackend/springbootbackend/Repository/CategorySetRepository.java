package ch.hslu.springbootbackend.springbootbackend.Repository;

import ch.hslu.springbootbackend.springbootbackend.Entity.Sets.CategorySet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategorySetRepository extends JpaRepository<CategorySet, Integer> {
    //Optional<CategorySet> findByCategoryId(Integer categorySetNumber);
    Optional<CategorySet> findByTitleAndCategorySetNumber(String title, String categorySetNumber);
    List<CategorySet> findByCategoryId(Integer categoryId);
    Optional<CategorySet> findByTitle(String title);
    Optional<CategorySet> findByCategorySetNumber(String number);
    long deleteAllByTitle(String title);
}
