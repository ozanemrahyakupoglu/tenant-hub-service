package com.obntech.tenanthub.service;

import com.obntech.tenanthub.dto.request.RealEstateCreateRequest;
import com.obntech.tenanthub.dto.request.RealEstateUpdateRequest;
import com.obntech.tenanthub.dto.response.RealEstateResponse;
import com.obntech.tenanthub.entity.RealEstateEntity;
import com.obntech.tenanthub.enums.Status;
import com.obntech.tenanthub.exception.BusinessException;
import com.obntech.tenanthub.exception.ErrorCode;
import com.obntech.tenanthub.mapper.RealEstateMapper;
import com.obntech.tenanthub.repository.RealEstateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class RealEstateService {

    private final RealEstateRepository realEstateRepository;
    private final RealEstateMapper realEstateMapper;

    @Transactional(readOnly = true)
    public RealEstateResponse getById(Long id) {
        log.info("Gayrimenkul getiriliyor. id: {}", id);
        return realEstateMapper.toResponse(findById(id));
    }

    @Transactional(readOnly = true)
    public Page<RealEstateResponse> getAll(Pageable pageable) {
        log.info("Gayrimenkuller listeleniyor.");
        return realEstateRepository.findAll(pageable).map(realEstateMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<RealEstateResponse> getAllByStatus(Status status, Pageable pageable) {
        log.info("Gayrimenkuller statüye göre listeleniyor. status: {}", status);
        return realEstateRepository.findAllByStatus(status, pageable).map(realEstateMapper::toResponse);
    }

    @Transactional
    public RealEstateResponse create(RealEstateCreateRequest request, String createdIp) {
        log.info("Gayrimenkul oluşturuluyor. name: {}", request.getName());

        if (realEstateRepository.existsByName(request.getName())) {
            throw new BusinessException("Bu gayrimenkul adı zaten kullanılıyor", ErrorCode.DUPLICATE_ENTRY, HttpStatus.CONFLICT);
        }

        RealEstateEntity entity = realEstateMapper.toEntity(request);
        entity.setCreatedBy("SYSTEM");
        entity.setCreatedDate(LocalDateTime.now());
        entity.setCreatedIp(createdIp);

        RealEstateEntity saved = realEstateRepository.save(entity);
        log.info("Gayrimenkul oluşturuldu. id: {}", saved.getId());
        return realEstateMapper.toResponse(saved);
    }

    @Transactional
    public RealEstateResponse update(Long id, RealEstateUpdateRequest request, String updatedIp) {
        log.info("Gayrimenkul güncelleniyor. id: {}", id);
        RealEstateEntity entity = findById(id);

        if (!entity.getName().equals(request.getName()) && realEstateRepository.existsByName(request.getName())) {
            throw new BusinessException("Bu gayrimenkul adı zaten kullanılıyor", ErrorCode.DUPLICATE_ENTRY, HttpStatus.CONFLICT);
        }

        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setType(request.getType());
        entity.setProvince(request.getProvince());
        entity.setDistrict(request.getDistrict());
        entity.setNeighborhood(request.getNeighborhood());
        entity.setAddress(request.getAddress());
        entity.setUpdatedBy("SYSTEM");
        entity.setUpdatedDate(LocalDateTime.now());
        entity.setUpdatedIp(updatedIp);

        log.info("Gayrimenkul güncellendi. id: {}", id);
        return realEstateMapper.toResponse(realEstateRepository.save(entity));
    }

    @Transactional
    public void changeStatus(Long id, Status status, String updatedIp) {
        log.info("Gayrimenkul durumu değiştiriliyor. id: {}, status: {}", id, status);
        RealEstateEntity entity = findById(id);
        entity.setStatus(status);
        entity.setUpdatedBy("SYSTEM");
        entity.setUpdatedDate(LocalDateTime.now());
        entity.setUpdatedIp(updatedIp);
        realEstateRepository.save(entity);
        log.info("Gayrimenkul durumu değiştirildi. id: {}", id);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Gayrimenkul siliniyor. id: {}", id);
        RealEstateEntity entity = findById(id);
        realEstateRepository.delete(entity);
        log.info("Gayrimenkul silindi. id: {}", id);
    }

    public RealEstateEntity findById(Long id) {
        return realEstateRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Gayrimenkul bulunamadı. id: " + id, ErrorCode.ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND));
    }
}
