package main.java.com.example.MIS.Invoicing.System.dto;


import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceResponseDTO {

    private Integer id;
    private Integer invoiceNo;
    private Integer estimatedId;
    private Integer chainId;
    private String serviceDetails;
    private Integer qty;
    private Float costPerQty;
    private Float amountPayable;
    private Float balance;
    private LocalDateTime dateOfPayment;
    private LocalDate dateOfService;
    private String deliveryDetails;
    private String emailId;
}