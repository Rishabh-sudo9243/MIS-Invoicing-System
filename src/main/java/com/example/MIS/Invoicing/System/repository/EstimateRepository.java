package main.java.com.example.MIS.Invoicing.System.repository;

import com.codeb.mis.model.Estimate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstimateRepository extends JpaRepository<Estimate, Integer> {

    List<Estimate> findByChainId(Integer chainId);

    List<Estimate> findByGroupName(String groupName);

    List<Estimate> findByBrandName(String brandName);

    boolean existsByChainId(Integer chainId);

    long countByChainId(Integer chainId);

    boolean existsByZoneName(String zoneName);
}