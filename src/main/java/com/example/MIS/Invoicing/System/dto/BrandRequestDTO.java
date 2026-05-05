package main.java.com.example.MIS.Invoicing.System.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BrandRequestDTO {

    @NotBlank(message = "Brand name is required")
    @Size(max = 50, message = "Brand name must not exceed 50 characters")
    private String brandName;

    @NotNull(message = "Company (Chain) must be selected")
    private Integer chainId;
}