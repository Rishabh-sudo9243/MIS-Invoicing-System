package com.example.MIS.Invoicing.System.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GroupRequestDTO {

    @NotBlank(message = "Group name must not be empty")
    private String groupName;
}