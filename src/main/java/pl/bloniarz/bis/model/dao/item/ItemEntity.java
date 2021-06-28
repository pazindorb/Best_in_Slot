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

    @Enumerated(EnumType.STRING)
    private ItemTypes type;

    @Enumerated(EnumType.STRING)
    private ItemSlots slot;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "ITEM_STAT",
            joinColumns = { @JoinColumn(name = "item_id") },
            inverseJoinColumns = { @JoinColumn(name = "equation_id") }
    )
    private List<StatsEquationEntity> stats;

    private double stamina;
    private double strength;
    private double agility;
    private double intelligence;
    private double secondary;
    private double mastery;
    private double criticalStrike;
    private double haste;
    private double versatility;

    private boolean old;
    private String dropInstance;
    private String wowheadLink;


    public void setStatsBothWay(List<StatsEquationEntity> statsEquationsDependingOnSlot) {
        statsEquationsDependingOnSlot.forEach(statsEquationEntity -> {
            List <ItemEntity> newList = statsEquationEntity.getItem();
            newList.add(this);
            statsEquationEntity.setItem(newList);
        });

        setStats(statsEquationsDependingOnSlot);
    }
}
