package pl.bloniarz.bis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.bloniarz.bis.model.dao.character.CharacterEntity;
import pl.bloniarz.bis.model.dao.equipmentset.EquipmentEntity;

import java.util.Optional;

@Repository
public interface EquipmentSetRepository extends JpaRepository<EquipmentEntity, Long> {

    Optional<EquipmentEntity> findByIdAndCharacterAndActiveIsTrue(long id, CharacterEntity characterEntity);

    @Query(value = "SELECT * " +
            "FROM equipment " +
            "WHERE character_id = ( " +
            "SELECT character_id " +
            "FROM characters " +
            "WHERE name = ?2 " +
            "AND active = true" +
            ") AND equipment_id = ?1 " +
            "AND active = true", nativeQuery = true)
    Optional<EquipmentEntity> findByIdAndCharacterName(long id, String character);

    @Query(value = "Select count(character_id) " +
            "from equipment " +
            "where character_id = ?1 " +
            "and active = true"
            , nativeQuery = true)
    int getCountOfSetForCharacterId(long id);
}