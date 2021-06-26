package pl.bloniarz.bis.model.dao.equipmentset;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.bloniarz.bis.model.dao.character.CharacterEntity;
import pl.bloniarz.bis.model.dao.item.ItemEntity;

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
    private ItemEntity head;

    @OneToOne
    private ItemEntity neck;

    @OneToOne
    private ItemEntity shoulders;

    @OneToOne
    private ItemEntity chest;

    @OneToOne
    private ItemEntity back;

    @OneToOne
    private ItemEntity wrists;

    @OneToOne
    private ItemEntity hands;

    @OneToOne
    private ItemEntity waist;

    @OneToOne
    private ItemEntity legs;

    @OneToOne
    private ItemEntity feet;

    @OneToOne
    private ItemEntity firstRing;

    @OneToOne
    private ItemEntity secondRing;

    @OneToOne
    private ItemEntity firstTrinket;

    @OneToOne
    private ItemEntity secondTrinket;

    @OneToOne
    private ItemEntity mainHand;

    @OneToOne
    private ItemEntity offHand;

}
