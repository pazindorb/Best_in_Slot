package pl.bloniarz.bis.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.bloniarz.bis.externalapi.WowheadHttpClient;
import pl.bloniarz.bis.externalapi.model.LootSource;
import pl.bloniarz.bis.externalapi.model.WowheadItemResponse;
import pl.bloniarz.bis.model.dao.user.UserAuthorityEntity;
import pl.bloniarz.bis.model.dao.user.UserEntity;
import pl.bloniarz.bis.repository.AuthorityRepository;
import pl.bloniarz.bis.repository.UserRepository;
import pl.bloniarz.bis.service.ItemService;

import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class FirstTimeRunConfig implements CommandLineRunner {

    private final WowheadHttpClient wowheadHttpClient;
    private final ItemService itemService;
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {
        //populateDatabaseWithWowheadItems();
        populateAuthoritiesAndCreateAdmin();
    }

    private void populateDatabaseWithWowheadItems() {
        for (LootSource value : LootSource.values()) {
            List<WowheadItemResponse> list = wowheadHttpClient.getItemsListForLootSource(value);
            itemService.addAllItemsToDatabase(list);
            System.out.println(value.getSourceName() + " : Added");
        }
    }

    private void populateAuthoritiesAndCreateAdmin(){
        List<UserAuthorityEntity> listOfAuthorities = new LinkedList<>();
        listOfAuthorities.add(authorityRepository.save(UserAuthorityEntity.builder()
                .authority("ROLE_ADMIN")
                .build()));
        listOfAuthorities.add(authorityRepository.save(UserAuthorityEntity.builder()
                .authority("ROLE_USER")
                .build()));

        String password = encoder.encode("adminpassword");

        UserEntity userEntity = UserEntity.builder()
                .email("bloniarzpatryk@gmail.pl")
                .login("administrator")
                .password(password)
                .authorities(listOfAuthorities)
                .build();

        userRepository.save(userEntity);
    }
}
