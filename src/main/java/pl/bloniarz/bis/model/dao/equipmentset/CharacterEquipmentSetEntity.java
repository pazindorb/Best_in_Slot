package pl.bloniarz.bis.model.dao.equipmentset;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.bloniarz.bis.model.dao.character.CharacterEntity;
import pl.bloniarz.bis.model.dao.item.ItemStatsEntity;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "CHARACTER_EQUIPMENT_SETS")
public class CharacterEquipmentSetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "set_id")
    private long id;

    private String name;
    private String specialization;

    @ManyToOne
    @JoinColumn(name = "character_id", unique = true)
    private CharacterEntity character;

    @OneToOne
    private ItemStatsEntity head;

    @OneToOne
    private ItemStatsEntity neck;

    @OneToOne
    private ItemStatsEntity shoulders;

    @OneToOne
    private ItemStatsEntity chest;

    @OneToOne
    private ItemStatsEntity back;

    @OneToOne
    private ItemStatsEntity wrists;

    @OneToOne
    private ItemStatsEntity hands;

    @OneToOne
    private ItemStatsEntity waist;

    @OneToOne
    private ItemStatsEntity legs;

    @OneToOne
    private ItemStatsEntity feet;

    @OneToOne
    private ItemStatsEntity firstRing;

    @OneToOne
    private ItemStatsEntity secondRing;

    @OneToOne
    private ItemStatsEntity firstTrinket;

    @OneToOne
    private ItemStatsEntity secondTrinket;

    @OneToOne
    private ItemStatsEntity mainHand;

    @OneToOne
    private ItemStatsEntity offHand;

}
