package pl.bloniarz.bis.model.dao.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "ITEMS_STATISTICS")
public class ItemStatsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stats_id")
    private long id;

    private boolean old;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private ItemEntity item;

    private int itemLevel;
    private int stamina;
    private int intelligence;
    private int strength;
    private int agility;

    private double secondary;
    private double mastery;
    private double criticalStrike;
    private double haste;
    private double versatility;



}
