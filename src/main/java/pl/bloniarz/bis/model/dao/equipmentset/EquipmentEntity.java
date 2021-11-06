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
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "character_id")
    private CharacterEntity character;

    @OneToOne
    private ItemSetEntity head;

    @OneToOne
    private ItemSetEntity neck;

    @OneToOne
    private ItemSetEntity shoulders;

    @OneToOne
    private ItemSetEntity chest;

    @OneToOne
    private ItemSetEntity back;

    @OneToOne
    private ItemSetEntity wrists;

    @OneToOne
    private ItemSetEntity hands;

    @OneToOne
    private ItemSetEntity waist;

    @OneToOne
    private ItemSetEntity legs;

    @OneToOne
    private ItemSetEntity feet;

    @OneToOne
    private ItemSetEntity firstRing;

    @OneToOne
    private ItemSetEntity secondRing;

    @OneToOne
    private ItemSetEntity firstTrinket;

    @OneToOne
    private ItemSetEntity secondTrinket;

    @OneToOne
    private ItemSetEntity mainHand;

    @OneToOne
    private ItemSetEntity offHand;

    public void delete(){
        name = "";
        specialization = "";
        active = false;
    }
}
