package main.java.com.example.MIS.Invoicing.System.controller;

import com.example.MIS.Invoicing.System.dto.BrandRequestDTO;
import com.example.MIS.Invoicing.System.dto.BrandResponseDTO;
import com.example.MIS.Invoicing.System.service.BrandService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/brands")
@CrossOrigin(origins = "*")
public class BrandController {

    @Autowired
    private BrandService brandService;

    // GET /api/brands               → all active brands
    // GET /api/brands?chainId=2     → filter by company
    // GET /api/brands?groupId=1     → filter by group
    @GetMapping
    public ResponseEntity<List<BrandResponseDTO>> getBrands(
            @RequestParam(required = false) Integer chainId,
            @RequestParam(required = false) Integer groupId) {

        List<BrandResponseDTO> result;

        if (chainId != null) {
            result = brandService.getBrandsByChain(chainId);
        } else if (groupId != null) {
            result = brandService.getBrandsByGroup(groupId);
        } else {
            result = brandService.getAllBrands();
        }

        return ResponseEntity.ok(result);
    }

    // GET /api/brands/count
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getCount() {
        return ResponseEntity.ok(Map.of("totalBrands", brandService.countBrands()));
    }

    // POST /api/brands
    @PostMapping
    public ResponseEntity<?> addBrand(@Valid @RequestBody BrandRequestDTO request) {
        try {
            BrandResponseDTO response = brandService.addBrand(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // PUT /api/brands/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBrand(
            @PathVariable Integer id,
            @Valid @RequestBody BrandRequestDTO request) {
        try {
            BrandResponseDTO response = brandService.updateBrand(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // DELETE /api/brands/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBrand(@PathVariable Integer id) {
        try {
            brandService.deleteBrand(id);
            return ResponseEntity.ok(Map.of("message", "Brand deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}