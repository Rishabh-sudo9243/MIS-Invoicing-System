package main.java.com.example.MIS.Invoicing.System.repository;

import com.codeb.mis.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

    Optional<Invoice> findByEstimatedId(Integer estimatedId);

    boolean existsByEstimatedId(Integer estimatedId);

    List<Invoice> findByChainId(Integer chainId);

    // Search by invoice_no, estimated_id, chain_id
    @Query("SELECT i FROM Invoice i WHERE " +
           "CAST(i.invoiceNo AS string) LIKE %:keyword% OR " +
           "CAST(i.estimatedId AS string) LIKE %:keyword% OR " +
           "CAST(i.chainId AS string) LIKE %:keyword%")
    List<Invoice> searchByKeyword(String keyword);

    // Auto-generate next 4-digit invoice number
    @Query("SELECT COALESCE(MAX(i.invoiceNo), 1000) FROM Invoice i")
    Integer findMaxInvoiceNo();
}