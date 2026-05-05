package main.java.com.example.MIS.Invoicing.System.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BrandResponseDTO {

    private Integer brandId;
    private String brandName;
    private Integer chainId;
    private String companyName;   // from Chain
    private Integer groupId;      // from Chain → Group
    private String groupName;     // from Chain → Group
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}