package com.obntech.tenanthub.security;

import com.obntech.tenanthub.entity.UserEntity;
import com.obntech.tenanthub.enums.Status;
import com.obntech.tenanthub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
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
        UserEntity user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı: " + username));

        return new User(
            user.getUsername(),
            user.getPasswordHash(),
            user.getStatus() == Status.ACTIVE,  // enabled
            true,                                 // accountNonExpired
            true,                                 // credentialsNonExpired
            user.getStatus() != Status.BLOCKED,   // accountNonLocked
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}