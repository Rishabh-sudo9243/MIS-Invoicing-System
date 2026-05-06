package main.java.com.example.MIS.Invoicing.System.controller;

import com.codeb.mis.dto.EstimateRequestDTO;
import com.codeb.mis.dto.EstimateResponseDTO;
import com.codeb.mis.service.EstimateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estimates")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EstimateController {

    private final EstimateService estimateService;

    // GET /api/estimates
    @GetMapping
    public ResponseEntity<List<EstimateResponseDTO>> getAll(
            @RequestParam(required = false) Integer chainId,
            @RequestParam(required = false) String groupName,
            @RequestParam(required = false) String brandName) {

        if (chainId != null)    return ResponseEntity.ok(estimateService.getEstimatesByChain(chainId));
        if (groupName != null)  return ResponseEntity.ok(estimateService.getEstimatesByGroup(groupName));
        if (brandName != null)  return ResponseEntity.ok(estimateService.getEstimatesByBrand(brandName));
        return ResponseEntity.ok(estimateService.getAllEstimates());
    }

    // GET /api/estimates/{id}
    @GetMapping("/{id}")
    public ResponseEntity<EstimateResponseDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(estimateService.getEstimateById(id));
    }

    // GET /api/estimates/count
    @GetMapping("/count")
    public ResponseEntity<Long> getCount() {
        return ResponseEntity.ok(estimateService.getTotalCount());
    }

    // POST /api/estimates
    @PostMapping
    public ResponseEntity<EstimateResponseDTO> create(@Valid @RequestBody EstimateRequestDTO dto) {
        return ResponseEntity.ok(estimateService.createEstimate(dto));
    }

    // PUT /api/estimates/{id}
    @PutMapping("/{id}")
    public ResponseEntity<EstimateResponseDTO> update(
            @PathVariable Integer id,
            @Valid @RequestBody EstimateRequestDTO dto) {
        return ResponseEntity.ok(estimateService.updateEstimate(id, dto));
    }

    // DELETE /api/estimates/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        estimateService.deleteEstimate(id);
        return ResponseEntity.ok("Estimate deleted successfully");
    }
}