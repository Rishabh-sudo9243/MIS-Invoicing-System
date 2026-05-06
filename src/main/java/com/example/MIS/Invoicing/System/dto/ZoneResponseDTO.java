package com.mis.invoicing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ZoneResponseDTO {

    private Integer zoneId;
    private String zoneName;
    private Boolean isActive;

    // Brand info
    private Integer brandId;
    private String brandName;

    // Chain info (Company)
    private Integer chainId;
    private String companyName;

    // Group info
    private Integer groupId;
    private String groupName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}