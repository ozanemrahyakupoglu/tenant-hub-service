package com.obntech.tenanthub.service;

import com.obntech.tenanthub.dto.request.PaymentCreateRequest;
import com.obntech.tenanthub.dto.request.PaymentUpdateRequest;
import com.obntech.tenanthub.dto.response.PaymentResponse;
import com.obntech.tenanthub.entity.PaymentEntity;
import com.obntech.tenanthub.enums.Status;
import com.obntech.tenanthub.exception.BusinessException;
import com.obntech.tenanthub.exception.ErrorCode;
import com.obntech.tenanthub.mapper.PaymentMapper;
import com.obntech.tenanthub.repository.PaymentRepository;
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
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final RentService rentService;
    private final PaymentMapper paymentMapper;

    @Transactional(readOnly = true)
    public PaymentResponse getById(Long id) {
        log.info("Ödeme kaydı getiriliyor. id: {}", id);
        return paymentMapper.toResponse(findById(id));
    }

    @Transactional(readOnly = true)
    public Page<PaymentResponse> getAll(Pageable pageable) {
        log.info("Ödeme kayıtları listeleniyor.");
        return paymentRepository.findAll(pageable).map(paymentMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<PaymentResponse> getAllByStatus(Status status, Pageable pageable) {
        log.info("Ödeme kayıtları statüye göre listeleniyor. status: {}", status);
        return paymentRepository.findAllByStatus(status, pageable).map(paymentMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<PaymentResponse> getAllByRentId(Long rentId, Pageable pageable) {
        log.info("Ödeme kayıtları kira kaydına göre listeleniyor. rentId: {}", rentId);
        return paymentRepository.findAllByRentId(rentId, pageable).map(paymentMapper::toResponse);
    }

    @Transactional
    public PaymentResponse create(PaymentCreateRequest request, String createdIp) {
        log.info("Ödeme kaydı oluşturuluyor. rentId: {}", request.getRentId());

        PaymentEntity entity = new PaymentEntity();
        entity.setRent(rentService.findById(request.getRentId()));
        entity.setAmount(request.getAmount());
        entity.setCurrency(request.getCurrency());
        entity.setPaymentDate(request.getPaymentDate());
        entity.setCreatedBy("SYSTEM");
        entity.setCreatedDate(LocalDateTime.now());
        entity.setCreatedIp(createdIp);

        PaymentEntity saved = paymentRepository.save(entity);
        log.info("Ödeme kaydı oluşturuldu. id: {}", saved.getId());
        return paymentMapper.toResponse(saved);
    }

    @Transactional
    public PaymentResponse update(Long id, PaymentUpdateRequest request, String updatedIp) {
        log.info("Ödeme kaydı güncelleniyor. id: {}", id);
        PaymentEntity entity = findById(id);

        entity.setRent(rentService.findById(request.getRentId()));
        entity.setAmount(request.getAmount());
        entity.setCurrency(request.getCurrency());
        entity.setPaymentDate(request.getPaymentDate());
        entity.setUpdatedBy("SYSTEM");
        entity.setUpdatedDate(LocalDateTime.now());
        entity.setUpdatedIp(updatedIp);

        log.info("Ödeme kaydı güncellendi. id: {}", id);
        return paymentMapper.toResponse(paymentRepository.save(entity));
    }

    @Transactional
    public void changeStatus(Long id, Status status, String updatedIp) {
        log.info("Ödeme kaydı durumu değiştiriliyor. id: {}, status: {}", id, status);
        PaymentEntity entity = findById(id);
        entity.setStatus(status);
        entity.setUpdatedBy("SYSTEM");
        entity.setUpdatedDate(LocalDateTime.now());
        entity.setUpdatedIp(updatedIp);
        paymentRepository.save(entity);
        log.info("Ödeme kaydı durumu değiştirildi. id: {}", id);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Ödeme kaydı siliniyor. id: {}", id);
        PaymentEntity entity = findById(id);
        paymentRepository.delete(entity);
        log.info("Ödeme kaydı silindi. id: {}", id);
    }

    public PaymentEntity findById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Ödeme kaydı bulunamadı. id: " + id, ErrorCode.ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND));
    }
}
