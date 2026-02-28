package com.obntech.tenanthub.security;

import com.obntech.tenanthub.entity.UserEntity;
import com.obntech.tenanthub.enums.Status;
import com.obntech.tenanthub.repository.UserRepository;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Collections;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı: " + username));

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        // Rolleri ekle (ROLE_ prefix ile)
        user.getUserRoles().forEach(ur ->
            authorities.add(new SimpleGrantedAuthority("ROLE_" + ur.getRole().getName()))
        );

        // Permission'ları ekle
        user.getUserRoles().forEach(ur ->
            ur.getRole().getRolePermissions().forEach(rp ->
                authorities.add(new SimpleGrantedAuthority(rp.getPermission().getName()))
            )
        );
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