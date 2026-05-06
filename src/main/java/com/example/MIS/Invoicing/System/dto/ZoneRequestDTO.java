package com.mis.invoicing.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ZoneRequestDTO {

    @NotBlank(message = "Zone name is required")
    @Size(min = 2, max = 50, message = "Zone name must be between 2 and 50 characters")
    private String zoneName;

    @NotNull(message = "Brand ID is required")
    private Integer brandId;
}