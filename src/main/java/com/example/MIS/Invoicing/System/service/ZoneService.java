package main.java.com.example.MIS.Invoicing.System.service;

import com.mis.invoicing.dto.ZoneRequestDTO;
import com.mis.invoicing.dto.ZoneResponseDTO;
import com.mis.invoicing.entity.Brand;
import com.mis.invoicing.entity.Zone;
import com.mis.invoicing.repository.BrandRepository;
import com.mis.invoicing.repository.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ZoneService {

    private final ZoneRepository zoneRepository;
    private final BrandRepository brandRepository;

    // ── Helpers ──────────────────────────────────────────────

    private Brand fetchActiveBrand(Integer brandId) {
        return brandRepository.findById(brandId)
                .filter(b -> Boolean.TRUE.equals(b.getIsActive()))
                .orElseThrow(() -> new RuntimeException("Brand not found with ID: " + brandId));
    }

    private ZoneResponseDTO toDTO(Zone z) {
        Brand brand = z.getBrand();
        return ZoneResponseDTO.builder()
                .zoneId(z.getZoneId())
                .zoneName(z.getZoneName())
                .isActive(z.getIsActive())
                .brandId(brand.getBrandId())
                .brandName(brand.getBrandName())
                .chainId(brand.getChain().getChainId())
                .companyName(brand.getChain().getChainName())
                .groupId(brand.getChain().getGroup().getGroupId())
                .groupName(brand.getChain().getGroup().getGroupName())
                .createdAt(z.getCreatedAt())
                .updatedAt(z.getUpdatedAt())
                .build();
    }

    // ── Read ──────────────────────────────────────────────────

    public List<ZoneResponseDTO> getAllZones() {
        return zoneRepository.findByIsActiveTrue()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ZoneResponseDTO> getZonesByBrand(Integer brandId) {
        return zoneRepository.findByBrand_BrandIdAndIsActiveTrue(brandId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ZoneResponseDTO> getZonesByChain(Integer chainId) {
        return zoneRepository.findByChainId(chainId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ZoneResponseDTO> getZonesByGroup(Integer groupId) {
        return zoneRepository.findByGroupId(groupId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public long countZones() {
        return zoneRepository.countByIsActiveTrue();
    }

    // ── Create ────────────────────────────────────────────────

    public ZoneResponseDTO addZone(ZoneRequestDTO dto) {
        Brand brand = fetchActiveBrand(dto.getBrandId());

        if (zoneRepository.existsByZoneNameAndBrand(dto.getZoneName(), dto.getBrandId())) {
            throw new RuntimeException(
                "Zone '" + dto.getZoneName() + "' already exists under brand '" + brand.getBrandName() + "'");
        }

        Zone zone = Zone.builder()
                .zoneName(dto.getZoneName().trim())
                .brand(brand)
                .isActive(true)
                .build();

        return toDTO(zoneRepository.save(zone));
    }

    // ── Update ────────────────────────────────────────────────

    public ZoneResponseDTO updateZone(Integer zoneId, ZoneRequestDTO dto) {
        Zone zone = zoneRepository.findById(zoneId)
                .filter(z -> Boolean.TRUE.equals(z.getIsActive()))
                .orElseThrow(() -> new RuntimeException("Zone not found with ID: " + zoneId));

        Brand brand = fetchActiveBrand(dto.getBrandId());

        if (zoneRepository.existsByZoneNameAndBrandExcludingSelf(dto.getZoneName(), dto.getBrandId(), zoneId)) {
            throw new RuntimeException(
                "Zone '" + dto.getZoneName() + "' already exists under brand '" + brand.getBrandName() + "'");
        }

        zone.setZoneName(dto.getZoneName().trim());
        zone.setBrand(brand);

        return toDTO(zoneRepository.save(zone));
    }

    // ── Delete (Soft) ─────────────────────────────────────────

    public void deleteZone(Integer zoneId) {
        Zone zone = zoneRepository.findById(zoneId)
                .filter(z -> Boolean.TRUE.equals(z.getIsActive()))
                .orElseThrow(() -> new RuntimeException("Zone not found with ID: " + zoneId));


        boolean hasEstimates = estimateRepository.existsByZoneName(zone.getZoneName());
        if (hasEstimates) {
            throw new RuntimeException("Cannot delete zone '" + zone.getZoneName() + "' because it is linked to active estimates.");
        }
        zone.setIsActive(false);
        zoneRepository.save(zone);
    }
}