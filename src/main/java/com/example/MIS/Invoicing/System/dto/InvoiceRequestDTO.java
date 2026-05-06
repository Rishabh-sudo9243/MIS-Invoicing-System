package main.java.com.example.MIS.Invoicing.System.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceRequestDTO {

    @NotNull(message = "Estimated ID is required")
    private Integer estimatedId;

    @NotNull(message = "Chain ID is required")
    private Integer chainId;

    @NotBlank(message = "Service details are required")
    @Size(max = 50)
    private String serviceDetails;

    @NotNull @Min(1)
    private Integer qty;

    @NotNull @DecimalMin("0.0")
    private Float costPerQty;

    @NotNull @DecimalMin("0.0")
    private Float amountPayable;

    @DecimalMin("0.0")
    private Float balance;

    private String dateOfPayment;   // "yyyy-MM-dd HH:mm:ss"
    private String dateOfService;   // "yyyy-MM-dd"

    @Size(max = 100)
    private String deliveryDetails;

    @Email(message = "Invalid email address")
    @Size(max = 50)
    private String emailId;
}