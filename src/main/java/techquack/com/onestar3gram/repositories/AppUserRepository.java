package techquack.com.onestar3gram.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import techquack.com.onestar3gram.entities.AppUser;

import java.util.List;
import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Integer> {

    List<AppUser> findBy();

    Optional<AppUser> findByKeycloakId(String keycloakId);

    Optional<AppUser> findByUsername(String username);
}
