package pl.bloniarz.bis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.bloniarz.bis.model.dao.item.ItemEntity;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity,Long> {
}
