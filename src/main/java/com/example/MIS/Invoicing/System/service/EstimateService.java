package main.java.com.example.MIS.Invoicing.System.service;

import com.codeb.mis.dto.EstimateRequestDTO;
import com.codeb.mis.dto.EstimateResponseDTO;
import com.codeb.mis.model.Estimate;
import com.codeb.mis.repository.EstimateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EstimateService {

    private final EstimateRepository estimateRepository;

    // ── GET ALL ──────────────────────────────────────────────
    public List<EstimateResponseDTO> getAllEstimates() {
        return estimateRepository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    // ── GET BY ID ────────────────────────────────────────────
    public EstimateResponseDTO getEstimateById(Integer id) {
        Estimate estimate = estimateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estimate not found with id: " + id));
        return toResponse(estimate);
    }

    // ── GET BY CHAIN ─────────────────────────────────────────
    public List<EstimateResponseDTO> getEstimatesByChain(Integer chainId) {
        return estimateRepository.findByChainId(chainId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    // ── GET BY GROUP ─────────────────────────────────────────
    public List<EstimateResponseDTO> getEstimatesByGroup(String groupName) {
        return estimateRepository.findByGroupName(groupName)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    // ── GET BY BRAND ─────────────────────────────────────────
    public List<EstimateResponseDTO> getEstimatesByBrand(String brandName) {
        return estimateRepository.findByBrandName(brandName)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    // ── COUNT ────────────────────────────────────────────────
    public long getTotalCount() {
        return estimateRepository.count();
    }

    // ── CREATE ───────────────────────────────────────────────
    public EstimateResponseDTO createEstimate(EstimateRequestDTO dto) {
        Estimate estimate = new Estimate();
        mapDtoToEntity(dto, estimate);
        return toResponse(estimateRepository.save(estimate));
    }

    // ── UPDATE ───────────────────────────────────────────────
    public EstimateResponseDTO updateEstimate(Integer id, EstimateRequestDTO dto) {
        Estimate estimate = estimateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estimate not found with id: " + id));
        mapDtoToEntity(dto, estimate);
        return toResponse(estimateRepository.save(estimate));
    }

    // ── DELETE ───────────────────────────────────────────────
    public void deleteEstimate(Integer id) {
        Estimate estimate = estimateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estimate not found with id: " + id));
        // TODO: Module 6 — block delete if Invoice is linked to this estimate
        estimateRepository.delete(estimate);
    }

    // ── CHECK IF CHAIN HAS ESTIMATES (used by ChainService delete guard) ──
    public boolean existsByChainId(Integer chainId) {
        return estimateRepository.existsByChainId(chainId);
    }

    // ── HELPERS ──────────────────────────────────────────────
    private void mapDtoToEntity(EstimateRequestDTO dto, Estimate estimate) {
        estimate.setChainId(dto.getChainId());
        estimate.setGroupName(dto.getGroupName());
        estimate.setBrandName(dto.getBrandName());
        estimate.setZoneName(dto.getZoneName());
        estimate.setService(dto.getService());
        estimate.setQty(dto.getQty());
        estimate.setCostPerUnit(dto.getCostPerUnit());
        estimate.setTotalCost(dto.getTotalCost());
        estimate.setDeliveryDate(dto.getDeliveryDate());
        estimate.setDeliveryDetails(dto.getDeliveryDetails());
    }

    private EstimateResponseDTO toResponse(Estimate estimate) {
        EstimateResponseDTO dto = new EstimateResponseDTO();
        dto.setEstimatedId(estimate.getEstimatedId());
        dto.setChainId(estimate.getChainId());
        dto.setGroupName(estimate.getGroupName());
        dto.setBrandName(estimate.getBrandName());
        dto.setZoneName(estimate.getZoneName());
        dto.setService(estimate.getService());
        dto.setQty(estimate.getQty());
        dto.setCostPerUnit(estimate.getCostPerUnit());
        dto.setTotalCost(estimate.getTotalCost());
        dto.setDeliveryDate(estimate.getDeliveryDate());
        dto.setDeliveryDetails(estimate.getDeliveryDetails());
        dto.setCreatedAt(estimate.getCreatedAt());
        dto.setUpdatedAt(estimate.getUpdatedAt());
        return dto;
    }
}