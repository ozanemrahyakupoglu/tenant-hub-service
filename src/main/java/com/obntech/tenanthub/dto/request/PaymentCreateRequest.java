package com.obntech.tenanthub.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCreateRequest {

    @NotNull(message = "Kira kaydı ID boş olamaz")
    private Long rentId;

    @NotNull(message = "Tutar boş olamaz")
    private BigDecimal amount;

    @NotBlank(message = "Para birimi boş olamaz")
    @Size(max = 10, message = "Para birimi en fazla 10 karakter olabilir")
    private String currency;

    @NotNull(message = "Ödeme tarihi boş olamaz")
    private LocalDateTime paymentDate;
}
