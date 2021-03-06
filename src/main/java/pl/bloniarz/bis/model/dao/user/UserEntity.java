package pl.bloniarz.bis.model.dao.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.bloniarz.bis.model.dao.character.CharacterEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "USERS")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;

    @Column(unique = true)
    private String login;

    @Column(unique = true)
    private String email;

    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<CharacterEntity> charactersList;

    @ManyToMany
    private List<UserAuthorityEntity> authorities;
    
    private boolean active;

    public void delete(){
        active = false;
        login = String.valueOf(id);
        email = String.valueOf(id);
        password = "";
        authorities = new ArrayList<>();
        charactersList.forEach(CharacterEntity::delete);
    }

}
