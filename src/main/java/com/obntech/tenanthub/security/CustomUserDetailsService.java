package com.obntech.tenanthub.security;

import com.obntech.tenanthub.entity.UserEntity;
import com.obntech.tenanthub.entity.UserRoleEntity;
import com.obntech.tenanthub.entity.RolePermissionEntity;
import com.obntech.tenanthub.enums.Status;
import com.obntech.tenanthub.repository.UserRepository;
import com.obntech.tenanthub.repository.UserRoleRepository;
import com.obntech.tenanthub.repository.RolePermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RolePermissionRepository rolePermissionRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı: " + username));

        List<UserRoleEntity> userRoles = userRoleRepository.findAllByUser_Id(user.getId());

        List<GrantedAuthority> authorities = new ArrayList<>();

        for (UserRoleEntity userRole : userRoles) {
            // ROLE_ prefix ile rol ekle
            authorities.add(new SimpleGrantedAuthority("ROLE_" + userRole.getRole().getName()));

            // Bu role ait permission'ları ekle
            List<RolePermissionEntity> rolePermissions =
                rolePermissionRepository.findAllByRole_Id(userRole.getRole().getId());
            for (RolePermissionEntity rp : rolePermissions) {
                authorities.add(new SimpleGrantedAuthority(rp.getPermission().getName()));
            }
        }

        return new User(
            user.getUsername(),
            user.getPasswordHash(),
            user.getStatus() == Status.ACTIVE,
            true,
            true,
            user.getStatus() != Status.BLOCKED,
            authorities
        );
    }
}
