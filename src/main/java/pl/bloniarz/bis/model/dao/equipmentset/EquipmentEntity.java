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
@Table(name = "EQUIPMENT")
public class EquipmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equipment_id")
    private long id;

    private String name;
    private String specialization;

    @ManyToOne
    @JoinColumn(name = "character_id")
    private CharacterEntity character;

    @OneToOne(orphanRemoval = true)
    private ItemSetEntity head;

    @OneToOne(orphanRemoval = true)
    private ItemSetEntity neck;

    @OneToOne(orphanRemoval = true)
    private ItemSetEntity shoulders;

    @OneToOne(orphanRemoval = true)
    private ItemSetEntity chest;

    @OneToOne(orphanRemoval = true)
    private ItemSetEntity back;

    @OneToOne(orphanRemoval = true)
    private ItemSetEntity wrists;

    @OneToOne(orphanRemoval = true)
    private ItemSetEntity hands;

    @OneToOne(orphanRemoval = true)
    private ItemSetEntity waist;

    @OneToOne(orphanRemoval = true)
    private ItemSetEntity legs;

    @OneToOne(orphanRemoval = true)
    private ItemSetEntity feet;

    @OneToOne(orphanRemoval = true)
    private ItemSetEntity firstRing;

    @OneToOne(orphanRemoval = true)
    private ItemSetEntity secondRing;

    @OneToOne(orphanRemoval = true)
    private ItemSetEntity firstTrinket;

    @OneToOne(orphanRemoval = true)
    private ItemSetEntity secondTrinket;

    @OneToOne(orphanRemoval = true)
    private ItemSetEntity mainHand;

    @OneToOne(orphanRemoval = true)
    private ItemSetEntity offHand;

}
