package pl.mr.springsecuritytraining.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.mr.springsecuritytraining.models.ERole;
import pl.mr.springsecuritytraining.models.Role;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(ERole role);
}
