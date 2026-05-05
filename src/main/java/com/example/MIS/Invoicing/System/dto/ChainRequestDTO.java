package com.example.MIS.Invoicing.System.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChainRequestDTO {

    @NotBlank(message = "Company name must not be empty")
    private String companyName;

    @NotBlank(message = "GSTN number must not be empty")
    private String gstnNo;

    @NotNull(message = "Group must be selected")
    private Integer groupId;
}