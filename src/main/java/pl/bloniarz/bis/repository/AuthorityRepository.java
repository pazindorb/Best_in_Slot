package pl.bloniarz.bis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.bloniarz.bis.model.dao.user.UserAuthorityEntity;

@Repository
public interface AuthorityRepository extends JpaRepository<UserAuthorityEntity, Long> {
    UserAuthorityEntity findByAuthority(String authority);
}
