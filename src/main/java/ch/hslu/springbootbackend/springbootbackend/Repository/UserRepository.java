package ch.hslu.springbootbackend.springbootbackend.Repository;


import ch.hslu.springbootbackend.springbootbackend.Entity.SchoolClass;
import ch.hslu.springbootbackend.springbootbackend.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);
	Optional<User> findByEmail(String email);
	Optional<User> findByResetPasswordToken(String resetPasswordToken);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);

	List<User> findAllByInSchoolClasses(SchoolClass schoolClass);

}
