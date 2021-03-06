package pl.bloniarz.bis.model.dao.character;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.bloniarz.bis.model.dao.equipmentset.EquipmentEntity;
import pl.bloniarz.bis.model.dao.user.UserEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "CHARACTERS")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CharacterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "character_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private String name;

    @Enumerated
    private CharacterClasses characterClass;

    @OneToMany(mappedBy = "character", cascade = CascadeType.ALL)
    private List<EquipmentEntity> characterEquipmentSets;

    private boolean active;

    public void delete(){
        name = "";
        active = false;
        characterEquipmentSets.forEach(EquipmentEntity::delete);
    }

}
