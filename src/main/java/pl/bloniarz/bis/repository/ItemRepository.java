package pl.bloniarz.bis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.bloniarz.bis.model.dao.item.ItemEntity;
import pl.bloniarz.bis.model.dao.item.enums.ItemSlots;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity,Long> {

    List<ItemEntity> findBySlotAndOldIsFalse(ItemSlots slot);

}
