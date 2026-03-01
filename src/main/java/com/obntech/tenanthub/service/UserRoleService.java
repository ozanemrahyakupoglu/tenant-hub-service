package com.obntech.tenanthub.service;

import com.obntech.tenanthub.dto.request.UserRoleRequest;
import com.obntech.tenanthub.dto.response.UserRoleResponse;
import com.obntech.tenanthub.entity.RoleEntity;
import com.obntech.tenanthub.entity.UserEntity;
import com.obntech.tenanthub.entity.UserRoleEntity;
import com.obntech.tenanthub.exception.BusinessException;
import com.obntech.tenanthub.exception.ErrorCode;
import com.obntech.tenanthub.mapper.UserRoleMapper;
import com.obntech.tenanthub.repository.UserRepository;
import com.obntech.tenanthub.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final UserRoleMapper userRoleMapper;

    @Transactional(readOnly = true)
    public List<UserRoleResponse> getByUserId(Long userId) {
        log.info("Kullanıcı rolleri getiriliyor. userId: {}", userId);
        return userRoleRepository.findAllByUser_Id(userId).stream()
                .map(userRoleMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<UserRoleResponse> getByRoleId(Long roleId) {
        log.info("Role atanmış kullanıcılar getiriliyor. roleId: {}", roleId);
        return userRoleRepository.findAllByRoleId(roleId).stream()
                .map(userRoleMapper::toResponse)
                .toList();
    }

    @Transactional
    public UserRoleResponse assign(UserRoleRequest request, String createdIp) {
        log.info("Kullanıcıya rol atanıyor. userId: {}, roleId: {}", request.getUserId(), request.getRoleId());

        if (userRoleRepository.existsByUserIdAndRoleId(request.getUserId(), request.getRoleId())) {
            throw new BusinessException("Bu kullanıcıya bu rol zaten atanmış", ErrorCode.DUPLICATE_ENTRY, HttpStatus.CONFLICT);
        }

        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new BusinessException("User with id " + request.getUserId() + " not found", ErrorCode.ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND));
        RoleEntity role = roleService.findById(request.getRoleId());

        UserRoleEntity entity = new UserRoleEntity();
        entity.setUser(user);
        entity.setRole(role);
        entity.setAssignedBy("SYSTEM");
        entity.setAssignedDate(LocalDateTime.now());
        entity.setCreatedBy("SYSTEM");
        entity.setCreatedDate(LocalDateTime.now());
        entity.setCreatedIp(createdIp);

        UserRoleEntity saved = userRoleRepository.save(entity);
        log.info("Kullanıcıya rol atandı. id: {}", saved.getId());
        return userRoleMapper.toResponse(saved);
    }

    @Transactional
    public void remove(Long id) {
        log.info("Kullanıcı rolü kaldırılıyor. id: {}", id);
        UserRoleEntity entity = userRoleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("UserRole with id " + id + " not found", ErrorCode.ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND));
        userRoleRepository.delete(entity);
        log.info("Kullanıcı rolü kaldırıldı. id: {}", id);
    }
}
