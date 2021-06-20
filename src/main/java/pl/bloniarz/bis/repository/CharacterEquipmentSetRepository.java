package pl.bloniarz.bis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.bloniarz.bis.model.dao.equipmentset.CharacterEquipmentSetEntity;

@Repository
public interface CharacterEquipmentSetRepository extends JpaRepository<CharacterEquipmentSetEntity, Long> {
}
