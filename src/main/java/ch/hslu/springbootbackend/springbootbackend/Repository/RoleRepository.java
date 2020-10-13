package ch.hslu.springbootbackend.springbootbackend.Repository;


import ch.hslu.springbootbackend.springbootbackend.Entity.ERole;
import ch.hslu.springbootbackend.springbootbackend.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(ERole name);
}
