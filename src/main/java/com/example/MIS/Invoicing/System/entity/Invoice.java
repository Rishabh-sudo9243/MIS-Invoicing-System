package main.java.com.example.MIS.Invoicing.System.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "invoice_no", unique = true, nullable = false)
    private Integer invoiceNo;

    @Column(name = "estimated_id", nullable = false)
    private Integer estimatedId;

    @Column(name = "chain_id", nullable = false)
    private Integer chainId;

    @Column(name = "service_details", length = 50)
    private String serviceDetails;

    @Column(name = "qty")
    private Integer qty;

    @Column(name = "cost_per_qty")
    private Float costPerQty;

    @Column(name = "amount_payable")
    private Float amountPayable;

    @Column(name = "balance")
    private Float balance;

    @Column(name = "date_of_payment")
    private LocalDateTime dateOfPayment;

    @Column(name = "date_of_service")
    private LocalDate dateOfService;

    @Column(name = "delivery_details", length = 100)
    private String deliveryDetails;

    @Column(name = "email_id", length = 50)
    private String emailId;
}