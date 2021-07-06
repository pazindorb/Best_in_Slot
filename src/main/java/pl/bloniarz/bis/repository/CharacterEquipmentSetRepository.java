package pl.bloniarz.bis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.bloniarz.bis.model.dao.character.CharacterEntity;
import pl.bloniarz.bis.model.dao.equipmentset.EquipmentEntity;

import java.util.Optional;

@Repository
public interface CharacterEquipmentSetRepository extends JpaRepository<EquipmentEntity, Long> {

    Optional<EquipmentEntity> findByIdAndCharacter(long id, CharacterEntity characterEntity);

}