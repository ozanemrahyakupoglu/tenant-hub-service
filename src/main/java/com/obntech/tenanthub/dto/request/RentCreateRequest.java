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
public class RentCreateRequest {

    @NotNull(message = "Gayrimenkul ID boş olamaz")
    private Long realEstateId;

    @NotNull(message = "Kiralama tarihi boş olamaz")
    private LocalDateTime rentDate;

    @NotNull(message = "Kira tutarı boş olamaz")
    private BigDecimal rentAmount;

    @NotBlank(message = "Para birimi boş olamaz")
    @Size(max = 10, message = "Para birimi en fazla 10 karakter olabilir")
    private String currency;
}
