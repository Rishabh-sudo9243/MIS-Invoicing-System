package main.java.com.example.MIS.Invoicing.System.service;

import com.example.MIS.Invoicing.System.dto.BrandRequestDTO;
import com.example.MIS.Invoicing.System.dto.BrandResponseDTO;
import com.example.MIS.Invoicing.System.entity.Brand;
import com.example.MIS.Invoicing.System.entity.Chain;
import com.example.MIS.Invoicing.System.repository.BrandRepository;
import com.example.MIS.Invoicing.System.repository.ChainRepository;
import com.mis.invoicing.repository.ZoneRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrandService {

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ChainRepository chainRepository;

    private final ZoneRepository zoneRepository;

    // ─── Helpers ────────────────────────────────────────────────

    private Chain fetchActiveChain(Integer chainId) {
        return chainRepository.findById(chainId)
                .filter(c -> Boolean.TRUE.equals(c.getIsActive()))
                .orElseThrow(() -> new RuntimeException("Company not found or inactive"));
    }

    private BrandResponseDTO toDTO(Brand brand) {
        BrandResponseDTO dto = new BrandResponseDTO();
        dto.setBrandId(brand.getBrandId());
        dto.setBrandName(brand.getBrandName());
        dto.setIsActive(brand.getIsActive());
        dto.setCreatedAt(brand.getCreatedAt());
        dto.setUpdatedAt(brand.getUpdatedAt());

        if (brand.getChain() != null) {
            dto.setChainId(brand.getChain().getChainId());
            dto.setCompanyName(brand.getChain().getCompanyName());

            if (brand.getChain().getGroup() != null) {
                dto.setGroupId(brand.getChain().getGroup().getGroupId());
                dto.setGroupName(brand.getChain().getGroup().getGroupName());
            }
        }
        return dto;
    }

    // ─── Get All ────────────────────────────────────────────────

    public List<BrandResponseDTO> getAllBrands() {
        return brandRepository.findByIsActiveTrue()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    // ─── Filter by Chain (Company) ───────────────────────────────

    public List<BrandResponseDTO> getBrandsByChain(Integer chainId) {
        return brandRepository.findByChain_ChainIdAndIsActiveTrue(chainId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    // ─── Filter by Group ────────────────────────────────────────

    public List<BrandResponseDTO> getBrandsByGroup(Integer groupId) {
        return brandRepository.findByChain_Group_GroupIdAndIsActiveTrue(groupId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    // ─── Add Brand ──────────────────────────────────────────────

    public BrandResponseDTO addBrand(BrandRequestDTO request) {
        Chain chain = fetchActiveChain(request.getChainId());

        boolean duplicate = brandRepository
                .existsByBrandNameIgnoreCaseAndChain_ChainIdAndIsActiveTrue(
                        request.getBrandName(), request.getChainId());
        if (duplicate) {
            throw new RuntimeException(
                    "Brand '" + request.getBrandName() + "' already exists under this company");
        }

        Brand brand = new Brand();
        brand.setBrandName(request.getBrandName().trim());
        brand.setChain(chain);
        brand.setIsActive(true);

        return toDTO(brandRepository.save(brand));
    }

    // ─── Update Brand ───────────────────────────────────────────

    public BrandResponseDTO updateBrand(Integer brandId, BrandRequestDTO request) {
        Brand brand = brandRepository.findById(brandId)
                .filter(b -> Boolean.TRUE.equals(b.getIsActive()))
                .orElseThrow(() -> new RuntimeException("Brand not found or inactive"));

        Chain chain = fetchActiveChain(request.getChainId());

        boolean duplicate = brandRepository
                .existsByBrandNameIgnoreCaseAndChain_ChainIdAndIsActiveTrueAndBrandIdNot(
                        request.getBrandName(), request.getChainId(), brandId);
        if (duplicate) {
            throw new RuntimeException(
                    "Brand '" + request.getBrandName() + "' already exists under this company");
        }

        brand.setBrandName(request.getBrandName().trim());
        brand.setChain(chain);

        return toDTO(brandRepository.save(brand));
    }

    // ─── Soft Delete Brand ──────────────────────────────────────

    public void deleteBrand(Integer brandId) {
        Brand brand = brandRepository.findById(brandId)
                .filter(b -> Boolean.TRUE.equals(b.getIsActive()))
                .orElseThrow(() -> new RuntimeException("Brand not found or inactive"));

        if (zoneRepository.existsByBrand_BrandIdAndIsActiveTrue(brandId)) {
            throw new RuntimeException(
            "Cannot delete brand '" + brand.getBrandName() + "' because it has active zones linked to it.");
        }

        brand.setIsActive(false);
        brandRepository.save(brand);
    }

    // ─── Count ──────────────────────────────────────────────────

    public long countBrands() {
        return brandRepository.findByIsActiveTrue().size();
    }
}