package com.obntech.tenanthub.service;

import com.obntech.tenanthub.dto.request.PermissionCreateRequest;
import com.obntech.tenanthub.dto.request.PermissionUpdateRequest;
import com.obntech.tenanthub.dto.response.PermissionResponse;
import com.obntech.tenanthub.entity.PermissionEntity;
import com.obntech.tenanthub.exception.BusinessException;
import com.obntech.tenanthub.exception.ErrorCode;
import com.obntech.tenanthub.mapper.PermissionMapper;
import com.obntech.tenanthub.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    @Transactional(readOnly = true)
    public PermissionResponse getById(Long id) {
        log.info("İzin getiriliyor. id: {}", id);
        return permissionMapper.toResponse(findById(id));
    }

    @Transactional(readOnly = true)
    public Page<PermissionResponse> getAll(Pageable pageable) {
        log.info("İzinler listeleniyor.");
        return permissionRepository.findAll(pageable).map(permissionMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<PermissionResponse> getByModule(String module) {
        log.info("Modüle göre izinler listeleniyor. module: {}", module);
        return permissionRepository.findAllByModule(module).stream()
                .map(permissionMapper::toResponse)
                .toList();
    }

    @Transactional
    public PermissionResponse create(PermissionCreateRequest request, String createdIp) {
        log.info("İzin oluşturuluyor. name: {}", request.getName());

        if (permissionRepository.existsByName(request.getName())) {
            throw new BusinessException("Bu izin adı zaten kullanılıyor", ErrorCode.DUPLICATE_ENTRY, HttpStatus.CONFLICT);
        }
        if (permissionRepository.existsByModuleAndAction(request.getModule(), request.getAction())) {
            throw new BusinessException("Bu modül ve aksiyon kombinasyonu zaten mevcut", ErrorCode.DUPLICATE_ENTRY, HttpStatus.CONFLICT);
        }

        PermissionEntity entity = permissionMapper.toEntity(request);
        entity.setCreatedBy("SYSTEM");
        entity.setCreatedDate(LocalDateTime.now());
        entity.setCreatedIp(createdIp);

        PermissionEntity saved = permissionRepository.save(entity);
        log.info("İzin oluşturuldu. id: {}", saved.getId());
        return permissionMapper.toResponse(saved);
    }

    @Transactional
    public PermissionResponse update(Long id, PermissionUpdateRequest request, String updatedIp) {
        log.info("İzin güncelleniyor. id: {}", id);
        PermissionEntity entity = findById(id);

        if (!entity.getName().equals(request.getName()) && permissionRepository.existsByName(request.getName())) {
            throw new BusinessException("Bu izin adı zaten kullanılıyor", ErrorCode.DUPLICATE_ENTRY, HttpStatus.CONFLICT);
        }

        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setUpdatedBy("SYSTEM");
        entity.setUpdatedDate(LocalDateTime.now());
        entity.setUpdatedIp(updatedIp);

        log.info("İzin güncellendi. id: {}", id);
        return permissionMapper.toResponse(permissionRepository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        log.info("İzin siliniyor. id: {}", id);
        PermissionEntity entity = findById(id);
        permissionRepository.delete(entity);
        log.info("İzin silindi. id: {}", id);
    }

    public PermissionEntity findById(Long id) {
        return permissionRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Permission with id " + id + " not found", ErrorCode.ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND));
    }
}
