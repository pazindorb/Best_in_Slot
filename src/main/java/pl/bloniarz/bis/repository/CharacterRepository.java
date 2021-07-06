package pl.bloniarz.bis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.bloniarz.bis.model.dao.character.CharacterEntity;

import java.util.List;
import java.util.Optional;

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


    @Query(value = "SELECT * " +
            "FROM characters " +
            "WHERE user_id = ( " +
            "SELECT user_id " +
            "FROM users " +
            "WHERE login = ?1" +
            ") AND name = ?2", nativeQuery = true)
    Optional<CharacterEntity> findByUsernameAndCharacterName(String username, String characterName);

    Optional<CharacterEntity> findByName(String name);

}
