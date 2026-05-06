package main.java.com.example.MIS.Invoicing.System.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class EstimateResponseDTO {

    private Integer estimatedId;
    private Integer chainId;
    private String groupName;
    private String brandName;
    private String zoneName;
    private String service;
    private Integer qty;
    private Float costPerUnit;
    private Float totalCost;
    private LocalDate deliveryDate;
    private String deliveryDetails;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}