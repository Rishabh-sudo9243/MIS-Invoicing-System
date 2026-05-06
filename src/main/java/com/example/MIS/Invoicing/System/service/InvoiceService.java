package main.java.com.example.MIS.Invoicing.System.service;


import com.codeb.mis.dto.InvoiceRequestDTO;
import com.codeb.mis.dto.InvoiceResponseDTO;
import com.codeb.mis.model.Invoice;
import com.codeb.mis.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    // ─── Helpers ────────────────────────────────────────────────
    private InvoiceResponseDTO toDTO(Invoice inv) {
        return InvoiceResponseDTO.builder()
                .id(inv.getId())
                .invoiceNo(inv.getInvoiceNo())
                .estimatedId(inv.getEstimatedId())
                .chainId(inv.getChainId())
                .serviceDetails(inv.getServiceDetails())
                .qty(inv.getQty())
                .costPerQty(inv.getCostPerQty())
                .amountPayable(inv.getAmountPayable())
                .balance(inv.getBalance())
                .dateOfPayment(inv.getDateOfPayment())
                .dateOfService(inv.getDateOfService())
                .deliveryDetails(inv.getDeliveryDetails())
                .emailId(inv.getEmailId())
                .build();
    }

    private Integer generateInvoiceNo() {
        return invoiceRepository.findMaxInvoiceNo() + 1;
    }

    // ─── GET ALL ────────────────────────────────────────────────
    public List<InvoiceResponseDTO> getAllInvoices() {
        return invoiceRepository.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    // ─── GET BY ID ──────────────────────────────────────────────
    public InvoiceResponseDTO getInvoiceById(Integer id) {
        Invoice inv = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));
        return toDTO(inv);
    }

    // ─── GET BY ESTIMATED_ID ────────────────────────────────────
    public InvoiceResponseDTO getInvoiceByEstimateId(Integer estimatedId) {
        Invoice inv = invoiceRepository.findByEstimatedId(estimatedId)
                .orElseThrow(() -> new RuntimeException("Invoice not found for estimate: " + estimatedId));
        return toDTO(inv);
    }

    // ─── SEARCH ─────────────────────────────────────────────────
    public List<InvoiceResponseDTO> search(String keyword) {
        return invoiceRepository.searchByKeyword(keyword)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    // ─── COUNT ──────────────────────────────────────────────────
    public long countInvoices() {
        return invoiceRepository.count();
    }

    // ─── CREATE ─────────────────────────────────────────────────
    public InvoiceResponseDTO createInvoice(InvoiceRequestDTO dto) {
        if (invoiceRepository.existsByEstimatedId(dto.getEstimatedId())) {
            throw new RuntimeException("Invoice already exists for estimate ID: " + dto.getEstimatedId());
        }

        Invoice inv = Invoice.builder()
                .invoiceNo(generateInvoiceNo())
                .estimatedId(dto.getEstimatedId())
                .chainId(dto.getChainId())
                .serviceDetails(dto.getServiceDetails())
                .qty(dto.getQty())
                .costPerQty(dto.getCostPerQty())
                .amountPayable(dto.getAmountPayable())
                .balance(dto.getBalance() != null ? dto.getBalance() : 0f)
                .dateOfPayment(dto.getDateOfPayment() != null
                        ? LocalDateTime.parse(dto.getDateOfPayment(),
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        : LocalDateTime.now())
                .dateOfService(dto.getDateOfService() != null
                        ? LocalDate.parse(dto.getDateOfService())
                        : null)
                .deliveryDetails(dto.getDeliveryDetails())
                .emailId(dto.getEmailId())
                .build();

        return toDTO(invoiceRepository.save(inv));
    }

    // ─── UPDATE (email only) ─────────────────────────────────────
    public InvoiceResponseDTO updateInvoiceEmail(Integer id, String emailId) {
        Invoice inv = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));
        inv.setEmailId(emailId);
        return toDTO(invoiceRepository.save(inv));
    }

    // ─── DELETE ─────────────────────────────────────────────────
    public void deleteInvoice(Integer id) {
        if (!invoiceRepository.existsById(id)) {
            throw new RuntimeException("Invoice not found with id: " + id);
        }
        invoiceRepository.deleteById(id);
    }
}