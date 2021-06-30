package pl.bloniarz.bis.model.dao.equipmentset;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.bloniarz.bis.model.dao.item.ItemEntity;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ITEM_SET")
public class ItemSetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_set_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private ItemEntity item;

    private int itemLevel;
    private boolean socket;
}
