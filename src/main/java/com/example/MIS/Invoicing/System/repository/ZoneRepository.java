package com.mis.invoicing.repository;

import com.mis.invoicing.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, Integer> {

    // All active zones
    List<Zone> findByIsActiveTrue();

    // Filter by brand
    List<Zone> findByBrand_BrandIdAndIsActiveTrue(Integer brandId);

    // Filter by chain (via brand)
    @Query("SELECT z FROM Zone z WHERE z.brand.chain.chainId = :chainId AND z.isActive = true")
    List<Zone> findByChainId(@Param("chainId") Integer chainId);

    // Filter by group (via brand -> chain -> group)
    @Query("SELECT z FROM Zone z WHERE z.brand.chain.group.groupId = :groupId AND z.isActive = true")
    List<Zone> findByGroupId(@Param("groupId") Integer groupId);

    // Duplicate check: same zone_name under same brand
    @Query("SELECT COUNT(z) > 0 FROM Zone z WHERE LOWER(z.zoneName) = LOWER(:name) AND z.brand.brandId = :brandId AND z.isActive = true")
    boolean existsByZoneNameAndBrand(@Param("name") String name, @Param("brandId") Integer brandId);

    // Duplicate check excluding self (for update)
    @Query("SELECT COUNT(z) > 0 FROM Zone z WHERE LOWER(z.zoneName) = LOWER(:name) AND z.brand.brandId = :brandId AND z.isActive = true AND z.zoneId <> :zoneId")
    boolean existsByZoneNameAndBrandExcludingSelf(@Param("name") String name, @Param("brandId") Integer brandId, @Param("zoneId") Integer zoneId);

    // Count active zones
    long countByIsActiveTrue();

    // Check if any active zone is linked to a brand (for brand delete guard)
    boolean existsByBrand_BrandIdAndIsActiveTrue(Integer brandId);
}