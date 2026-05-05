package main.java.com.example.MIS.Invoicing.System.controller;

import com.example.MIS.Invoicing.System.dto.ChainRequestDTO;
import com.example.MIS.Invoicing.System.dto.ChainResponseDTO;
import com.example.MIS.Invoicing.System.service.ChainService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chains")
@RequiredArgsConstructor
public class ChainController {

    private final ChainService chainService;

    // GET /api/chains              → all active chains
    @GetMapping
    public ResponseEntity<List<ChainResponseDTO>> getAllChains() {
        return ResponseEntity.ok(chainService.getAllActiveChains());
    }

    // GET /api/chains/count
    @GetMapping("/count")
    public ResponseEntity<?> getChainCount() {
        return ResponseEntity.ok(
            java.util.Map.of("totalChains", chainService.countActiveChains())
        );
    }

    // GET /api/chains?groupId=1   → filter by group
    @GetMapping(params = "groupId")
    public ResponseEntity<List<ChainResponseDTO>> getChainsByGroup(
            @RequestParam Integer groupId) {
        return ResponseEntity.ok(chainService.getChainsByGroup(groupId));
    }

    // POST /api/chains
    @PostMapping
    public ResponseEntity<?> addChain(@Valid @RequestBody ChainRequestDTO dto) {
        try {
            return ResponseEntity.ok(chainService.addChain(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT /api/chains/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> updateChain(
            @PathVariable Integer id,
            @Valid @RequestBody ChainRequestDTO dto) {
        try {
            return ResponseEntity.ok(chainService.updateChain(id, dto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE /api/chains/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteChain(@PathVariable Integer id) {
        try {
            chainService.softDeleteChain(id);
            return ResponseEntity.ok("Chain deactivated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}