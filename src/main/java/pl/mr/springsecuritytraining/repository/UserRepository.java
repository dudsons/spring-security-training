package pl.mr.springsecuritytraining.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.mr.springsecuritytraining.models.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long > {
    Optional<User> findByName(String name);
    boolean existsUserByName(String name);
    boolean existsUserByEmail(String email);
}
