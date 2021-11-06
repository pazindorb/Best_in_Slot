package pl.bloniarz.bis.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.bloniarz.bis.model.dao.user.UserEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {

    Optional<UserEntity> findByLoginAndActiveIsTrue(String login);
    Optional<UserEntity> findByEmailAndActiveIsTrue(String email);
    Optional<UserEntity> findByIdAndActiveIsTrue(long id);

    Optional<UserEntity> findByLogin(String login);
    Optional<UserEntity> findByEmail(String email);

    List<UserEntity> findByActiveIsTrue(Pageable pageable);
}
