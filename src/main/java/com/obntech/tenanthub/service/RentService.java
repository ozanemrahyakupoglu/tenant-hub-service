package com.obntech.tenanthub.service;

import com.obntech.tenanthub.dto.request.RentCreateRequest;
import com.obntech.tenanthub.dto.request.RentUpdateRequest;
import com.obntech.tenanthub.dto.response.RentResponse;
import com.obntech.tenanthub.entity.RentEntity;
import com.obntech.tenanthub.enums.Status;
import com.obntech.tenanthub.exception.BusinessException;
import com.obntech.tenanthub.exception.ErrorCode;
import com.obntech.tenanthub.mapper.RentMapper;
import com.obntech.tenanthub.repository.RentRepository;
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
public class RentService {

    private final RentRepository rentRepository;
    private final RealEstateService realEstateService;
    private final RentMapper rentMapper;

    @Transactional(readOnly = true)
    public RentResponse getById(Long id) {
        log.info("Kira kaydı getiriliyor. id: {}", id);
        return rentMapper.toResponse(findById(id));
    }

    @Transactional(readOnly = true)
    public Page<RentResponse> getAll(Pageable pageable) {
        log.info("Kira kayıtları listeleniyor.");
        return rentRepository.findAll(pageable).map(rentMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<RentResponse> getAllByStatus(Status status, Pageable pageable) {
        log.info("Kira kayıtları statüye göre listeleniyor. status: {}", status);
        return rentRepository.findAllByStatus(status, pageable).map(rentMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<RentResponse> getAllByRealEstateId(Long realEstateId, Pageable pageable) {
        log.info("Kira kayıtları gayrimenkule göre listeleniyor. realEstateId: {}", realEstateId);
        return rentRepository.findAllByRealEstateId(realEstateId, pageable).map(rentMapper::toResponse);
    }

    @Transactional
    public RentResponse create(RentCreateRequest request, String createdIp) {
        log.info("Kira kaydı oluşturuluyor. realEstateId: {}", request.getRealEstateId());

        RentEntity entity = new RentEntity();
        entity.setRealEstate(realEstateService.findById(request.getRealEstateId()));
        entity.setRentDate(request.getRentDate());
        entity.setRentAmount(request.getRentAmount());
        entity.setCurrency(request.getCurrency());
        entity.setCreatedBy("SYSTEM");
        entity.setCreatedDate(LocalDateTime.now());
        entity.setCreatedIp(createdIp);

        RentEntity saved = rentRepository.save(entity);
        log.info("Kira kaydı oluşturuldu. id: {}", saved.getId());
        return rentMapper.toResponse(saved);
    }

    @Transactional
    public RentResponse update(Long id, RentUpdateRequest request, String updatedIp) {
        log.info("Kira kaydı güncelleniyor. id: {}", id);
        RentEntity entity = findById(id);

        entity.setRealEstate(realEstateService.findById(request.getRealEstateId()));
        entity.setRentDate(request.getRentDate());
        entity.setRentAmount(request.getRentAmount());
        entity.setCurrency(request.getCurrency());
        entity.setUpdatedBy("SYSTEM");
        entity.setUpdatedDate(LocalDateTime.now());
        entity.setUpdatedIp(updatedIp);

        log.info("Kira kaydı güncellendi. id: {}", id);
        return rentMapper.toResponse(rentRepository.save(entity));
    }

    @Transactional
    public void changeStatus(Long id, Status status, String updatedIp) {
        log.info("Kira kaydı durumu değiştiriliyor. id: {}, status: {}", id, status);
        RentEntity entity = findById(id);
        entity.setStatus(status);
        entity.setUpdatedBy("SYSTEM");
        entity.setUpdatedDate(LocalDateTime.now());
        entity.setUpdatedIp(updatedIp);
        rentRepository.save(entity);
        log.info("Kira kaydı durumu değiştirildi. id: {}", id);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Kira kaydı siliniyor. id: {}", id);
        RentEntity entity = findById(id);
        rentRepository.delete(entity);
        log.info("Kira kaydı silindi. id: {}", id);
    }

    public RentEntity findById(Long id) {
        return rentRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Kira kaydı bulunamadı. id: " + id, ErrorCode.ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND));
    }
}
