package com.example.MIS.Invoicing.System.repository;

import com.example.MIS.Invoicing.System.entity.Chain;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChainRepository extends JpaRepository<Chain, Integer> {

    // All active chains
    List<Chain> findByIsActiveTrue();

    // Filter active chains by group
    List<Chain> findByGroup_GroupIdAndIsActiveTrue(Integer groupId);

    // Duplicate GSTN check on create
    boolean existsByGstnNo(String gstnNo);

    // Duplicate GSTN check on update (exclude self)
    boolean existsByGstnNoAndChainIdNot(String gstnNo, Integer chainId);

    // Used by Group soft-delete check
    boolean existsByGroup_GroupIdAndIsActiveTrue(Integer groupId);
}