package pl.bloniarz.bis.model.dao.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.bloniarz.bis.model.dao.item.enums.ItemSlots;
import pl.bloniarz.bis.model.dao.item.enums.ItemTypes;

import javax.persistence.*;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "ITEMS")
public class ItemEntity {

    @Id
    @Column(name = "item_id")
    private long id;

    private String name;

    @Enumerated(EnumType.ORDINAL)
    private ItemTypes type;

    @Enumerated(EnumType.ORDINAL)
    private ItemSlots slot;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval=true)
    private List<ItemStatsEntity> stats;

    private String dropInstance;
    private String wowheadLink;

}
