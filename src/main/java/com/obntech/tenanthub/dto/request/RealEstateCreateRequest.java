package com.obntech.tenanthub.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RealEstateCreateRequest {

    @NotBlank(message = "Gayrimenkul adı boş olamaz")
    @Size(max = 200, message = "Gayrimenkul adı en fazla 200 karakter olabilir")
    private String name;

    @Size(max = 1000, message = "Açıklama en fazla 1000 karakter olabilir")
    private String description;

    @NotBlank(message = "Tip boş olamaz")
    @Size(max = 50, message = "Tip en fazla 50 karakter olabilir")
    private String type;

    @NotBlank(message = "İl boş olamaz")
    @Size(max = 100, message = "İl en fazla 100 karakter olabilir")
    private String province;

    @NotBlank(message = "İlçe boş olamaz")
    @Size(max = 100, message = "İlçe en fazla 100 karakter olabilir")
    private String district;

    @NotBlank(message = "Mahalle boş olamaz")
    @Size(max = 100, message = "Mahalle en fazla 100 karakter olabilir")
    private String neighborhood;

    @NotBlank(message = "Adres boş olamaz")
    @Size(max = 500, message = "Adres en fazla 500 karakter olabilir")
    private String address;

    private Long tenantId;

    private Long landlordId;
}
