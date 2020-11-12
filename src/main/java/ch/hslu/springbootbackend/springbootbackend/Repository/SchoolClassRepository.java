package ch.hslu.springbootbackend.springbootbackend.Repository;

import ch.hslu.springbootbackend.springbootbackend.Entity.Institution;
import ch.hslu.springbootbackend.springbootbackend.Entity.SchoolClass;
import ch.hslu.springbootbackend.springbootbackend.Entity.Sets.ExamSet;
import ch.hslu.springbootbackend.springbootbackend.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SchoolClassRepository extends JpaRepository<SchoolClass, Integer> {
    Optional<SchoolClass> findByName(String name);
    List<SchoolClass> findAllByUsersInClass(User user);
    List<SchoolClass> findAllByExamSetsForSchoolClass(ExamSet examSet);
    List<SchoolClass> findAllByInstitution(Institution institution);
}
