package com.example.MIS.Invoicing.System.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChainResponseDTO {
    private Integer chainId;
    private String companyName;
    private String gstnNo;
    private Integer groupId;
    private String groupName;   // shown in dashboard table
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}