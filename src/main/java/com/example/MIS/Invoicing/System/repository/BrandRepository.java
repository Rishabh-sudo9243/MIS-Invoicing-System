package main.java.com.example.MIS.Invoicing.System.repository;

import com.example.MIS.Invoicing.System.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {

    // All active brands
    List<Brand> findByIsActiveTrue();

    // Filter by chain (company)
    List<Brand> findByChain_ChainIdAndIsActiveTrue(Integer chainId);

    // Filter by group (via chain → group)
    List<Brand> findByChain_Group_GroupIdAndIsActiveTrue(Integer groupId);

    // Duplicate brand name check within same chain
    boolean existsByBrandNameIgnoreCaseAndChain_ChainIdAndIsActiveTrue(String brandName, Integer chainId);

    // Duplicate check excluding self (for update)
    boolean existsByBrandNameIgnoreCaseAndChain_ChainIdAndIsActiveTrueAndBrandIdNot(
            String brandName, Integer chainId, Integer brandId);

    boolean existsByChain_ChainIdAndIsActiveTrue(Integer chainId);

    // Check if brand is linked to any active zone (for delete guard)
    // Will be used once SubZone entity is created — stub for now
    // boolean existsByBrandIdAndZones_IsActiveTrue(Integer brandId);
}