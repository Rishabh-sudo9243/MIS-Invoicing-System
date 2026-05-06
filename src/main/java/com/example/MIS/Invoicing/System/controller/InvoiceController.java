package main.java.com.example.MIS.Invoicing.System.controller;

import com.codeb.mis.dto.InvoiceRequestDTO;
import com.codeb.mis.dto.InvoiceResponseDTO;
import com.codeb.mis.service.InvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class InvoiceController {

    private final InvoiceService invoiceService;

    // GET all or search
    @GetMapping
    public ResponseEntity<List<InvoiceResponseDTO>> getAll(
            @RequestParam(required = false) String search) {
        if (search != null && !search.isBlank()) {
            return ResponseEntity.ok(invoiceService.search(search));
        }
        return ResponseEntity.ok(invoiceService.getAllInvoices());
    }

    // GET by ID
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponseDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(invoiceService.getInvoiceById(id));
    }

    // GET by estimatedId
    @GetMapping("/by-estimate/{estimatedId}")
    public ResponseEntity<InvoiceResponseDTO> getByEstimate(
            @PathVariable Integer estimatedId) {
        return ResponseEntity.ok(invoiceService.getInvoiceByEstimateId(estimatedId));
    }

    // GET count
    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(invoiceService.countInvoices());
    }

    // POST create
    @PostMapping
    public ResponseEntity<InvoiceResponseDTO> create(
            @Valid @RequestBody InvoiceRequestDTO dto) {
        return ResponseEntity.ok(invoiceService.createInvoice(dto));
    }

    // PUT update email only
    @PutMapping("/{id}/email")
    public ResponseEntity<InvoiceResponseDTO> updateEmail(
            @PathVariable Integer id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(
            invoiceService.updateInvoiceEmail(id, body.get("emailId")));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        invoiceService.deleteInvoice(id);
        return ResponseEntity.ok("Invoice deleted successfully");
    }
}