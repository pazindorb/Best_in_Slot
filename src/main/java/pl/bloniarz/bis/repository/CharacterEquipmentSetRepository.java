package pl.bloniarz.bis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.bloniarz.bis.model.dao.character.CharacterEntity;
import pl.bloniarz.bis.model.dao.equipmentset.CharacterEquipmentSetEntity;

import java.util.Optional;

@Repository
public interface CharacterEquipmentSetRepository extends JpaRepository<CharacterEquipmentSetEntity, Long> {

    Optional<CharacterEquipmentSetEntity> findByIdAndCharacter(long id, CharacterEntity characterEntity);

    //void update(CharacterEquipmentSetEntity setEntity);
}