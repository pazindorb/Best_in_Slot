package pl.bloniarz.bis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.bloniarz.bis.model.dao.character.CharacterEntity;

import java.util.List;

@Repository
public interface CharacterRepository extends JpaRepository<CharacterEntity, Long> {

    @Query(value = "SELECT * " +
            "FROM characters " +
            "WHERE user_id = ( " +
                "SELECT user_id " +
                "FROM users " +
                "WHERE login = ?1" +
            ")", nativeQuery = true)
    List<CharacterEntity> findAllCharactersOfUserNamed(String username);
}
