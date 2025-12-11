package io.github.mrlevi1112.authservice.security;

import io.github.mrlevi1112.authservice.common.constants.AuthServiceConstants;
import io.github.mrlevi1112.authservice.model.User;
import io.github.mrlevi1112.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(AuthServiceConstants.Security.USERNAME_NOT_FOUND + username));

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(
                AuthServiceConstants.Security.USER_ROLE + user.getRole().name()
        );

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(authority)
        );
    }
}