package main.java.com.example.MIS.Invoicing.System.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class EstimateRequestDTO {

    @NotNull(message = "Chain ID is required")
    private Integer chainId;

    @NotBlank(message = "Group name is required")
    @Size(max = 50, message = "Group name must be under 50 characters")
    private String groupName;

    @NotBlank(message = "Brand name is required")
    @Size(max = 50, message = "Brand name must be under 50 characters")
    private String brandName;

    @NotBlank(message = "Zone name is required")
    @Size(max = 50, message = "Zone name must be under 50 characters")
    private String zoneName;

    @NotBlank(message = "Service details are required")
    @Size(max = 100, message = "Service must be under 100 characters")
    private String service;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer qty;

    @NotNull(message = "Cost per unit is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Cost per unit must be greater than 0")
    private Float costPerUnit;

    @NotNull(message = "Total cost is required")
    private Float totalCost;

    private LocalDate deliveryDate;

    @Size(max = 100, message = "Delivery details must be under 100 characters")
    private String deliveryDetails;
}