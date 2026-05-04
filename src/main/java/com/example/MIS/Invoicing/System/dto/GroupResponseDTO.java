package com.example.MIS.Invoicing.System.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class GroupResponseDTO {
    private Integer groupId;
    private String groupName;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}