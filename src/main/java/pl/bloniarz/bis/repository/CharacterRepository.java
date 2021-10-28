package pl.bloniarz.bis.repository;

import net.bytebuddy.asm.TypeReferenceAdjustment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.bloniarz.bis.model.dao.character.CharacterEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface CharacterRepository extends JpaRepository<CharacterEntity, Long> {

    @Query(value = "SELECT * " +
            "FROM characters " +
            "WHERE active = true " +
            "AND user_id = ( " +
                "SELECT user_id " +
                "FROM users " +
                "WHERE login = ?1)", nativeQuery = true)
    List<CharacterEntity> findAllCharactersOfUserNamed(String username);

    @Query(value = "SELECT * " +
            "FROM characters " +
            "WHERE active = true " +
            "AND user_id = ?1 ", nativeQuery = true)
    List<CharacterEntity> findAllCharactersOfUserId(long id);

    @Query(value = "SELECT * " +
            "FROM characters " +
            "WHERE user_id = ( " +
            "SELECT user_id " +
            "FROM users " +
            "WHERE login = ?1 " +
            "AND active = true" +
            ") AND name = ?2 " +
            "AND active = true", nativeQuery = true)
    Optional<CharacterEntity> findByUsernameAndCharacterName(String username, String characterName);

    @Query(value = "Select count(user_id) " +
            "from characters " +
            "where user_id = ?1 " +
            "and active = true"
            , nativeQuery = true)
    int getCountOfCharactersForUserId(long id);


    Optional<CharacterEntity> findByIdAndActiveIsTrue(long id);
}
