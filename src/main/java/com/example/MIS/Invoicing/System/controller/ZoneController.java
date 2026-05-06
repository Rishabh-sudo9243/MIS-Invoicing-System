package main.java.com.example.MIS.Invoicing.System.controller;

import com.mis.invoicing.dto.ZoneRequestDTO;
import com.mis.invoicing.dto.ZoneResponseDTO;
import com.mis.invoicing.service.ZoneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/zones")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ZoneController {

    private final ZoneService zoneService;

    // GET /api/zones — all active zones
    // GET /api/zones?brandId=1 — filter by brand
    // GET /api/zones?chainId=2 — filter by chain/company
    // GET /api/zones?groupId=3 — filter by group
    @GetMapping
    public ResponseEntity<List<ZoneResponseDTO>> getZones(
            @RequestParam(required = false) Integer brandId,
            @RequestParam(required = false) Integer chainId,
            @RequestParam(required = false) Integer groupId) {

        if (brandId != null) return ResponseEntity.ok(zoneService.getZonesByBrand(brandId));
        if (chainId != null) return ResponseEntity.ok(zoneService.getZonesByChain(chainId));
        if (groupId != null) return ResponseEntity.ok(zoneService.getZonesByGroup(groupId));
        return ResponseEntity.ok(zoneService.getAllZones());
    }

    // GET /api/zones/count
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getCount() {
        return ResponseEntity.ok(Map.of("count", zoneService.countZones()));
    }

    // POST /api/zones
    @PostMapping
    public ResponseEntity<ZoneResponseDTO> addZone(@Valid @RequestBody ZoneRequestDTO dto) {
        return ResponseEntity.ok(zoneService.addZone(dto));
    }

    // PUT /api/zones/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ZoneResponseDTO> updateZone(
            @PathVariable Integer id,
            @Valid @RequestBody ZoneRequestDTO dto) {
        return ResponseEntity.ok(zoneService.updateZone(id, dto));
    }

    // DELETE /api/zones/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteZone(@PathVariable Integer id) {
        zoneService.deleteZone(id);
        return ResponseEntity.ok(Map.of("message", "Zone deleted successfully"));
    }
}