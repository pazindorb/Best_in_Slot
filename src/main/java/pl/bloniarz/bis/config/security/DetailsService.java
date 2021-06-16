package pl.bloniarz.bis.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.bloniarz.bis.model.dao.user.UserAuthorityEntity;
import pl.bloniarz.bis.model.dao.user.UserEntity;
import pl.bloniarz.bis.repository.UserRepository;

@Service
@Primary
@RequiredArgsConstructor
public class DetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", username)));

        return User.builder()
                .username(username)
                .password(userEntity.getPassword())
                .authorities(userEntity.getAuthorities()
                        .stream()
                        .map(UserAuthorityEntity::getAuthority)
                        .toArray(String[]::new))
                .build();
    }
}
