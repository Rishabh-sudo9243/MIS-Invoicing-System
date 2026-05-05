package com.example.MIS.Invoicing.System.service;

import com.example.MIS.Invoicing.System.dto.ChainRequestDTO;
import com.example.MIS.Invoicing.System.dto.ChainResponseDTO;
import com.example.MIS.Invoicing.System.entity.Chain;
import com.example.MIS.Invoicing.System.entity.Group;
import com.example.MIS.Invoicing.System.repository.ChainRepository;
import com.example.MIS.Invoicing.System.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import main.java.com.example.MIS.Invoicing.System.repository.BrandRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChainService {

    @Autowired
    private BrandRepository brandRepository;

    private final ChainRepository chainRepository;
    private final GroupRepository groupRepository;

    // ── GET ALL ACTIVE CHAINS ──────────────────────────────────────────────
    public List<ChainResponseDTO> getAllActiveChains() {
        return chainRepository.findByIsActiveTrue()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ── GET CHAINS FILTERED BY GROUP ───────────────────────────────────────
    public List<ChainResponseDTO> getChainsByGroup(Integer groupId) {
        return chainRepository.findByGroup_GroupIdAndIsActiveTrue(groupId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ── ADD CHAIN ──────────────────────────────────────────────────────────
    public ChainResponseDTO addChain(ChainRequestDTO dto) {
        if (chainRepository.existsByGstnNo(dto.getGstnNo().trim())) {
            throw new RuntimeException("GSTN number already exists!!!");
        }

        Group group = groupRepository.findById(dto.getGroupId())
                .orElseThrow(() -> new RuntimeException("Selected group not found"));

        if (!group.getIsActive()) {
            throw new RuntimeException("Selected group is inactive");
        }

        Chain chain = new Chain();
        chain.setCompanyName(dto.getCompanyName().trim());
        chain.setGstnNo(dto.getGstnNo().trim());
        chain.setGroup(group);
        chain.setIsActive(true);

        return toDTO(chainRepository.save(chain));
    }

    // ── UPDATE CHAIN ───────────────────────────────────────────────────────
    public ChainResponseDTO updateChain(Integer chainId, ChainRequestDTO dto) {
        Chain chain = chainRepository.findById(chainId)
                .orElseThrow(() -> new RuntimeException("Chain not found with id: " + chainId));

        if (chainRepository.existsByGstnNoAndChainIdNot(dto.getGstnNo().trim(), chainId)) {
            throw new RuntimeException("GSTN number already used by another company");
        }

        Group group = groupRepository.findById(dto.getGroupId())
                .orElseThrow(() -> new RuntimeException("Selected group not found"));

        chain.setCompanyName(dto.getCompanyName().trim());
        chain.setGstnNo(dto.getGstnNo().trim());
        chain.setGroup(group);

        return toDTO(chainRepository.save(chain));
    }

    // ── SOFT DELETE ────────────────────────────────────────────────────────
    // Chain can only be deleted if NOT linked to any Brand.
    // Placeholder — wire to BrandRepository once Brand module is built.
    public void softDeleteChain(Integer chainId) {
        Chain chain = chainRepository.findById(chainId)
                .orElseThrow(() -> new RuntimeException("Chain not found with id: " + chainId));

        boolean linkedToBrand = brandRepository.existsByChain_ChainIdAndIsActiveTrue(chainId);
        if (linkedToBrand) {
            throw new RuntimeException("Cannot delete chain linked to a brand");
        }

        chain.setIsActive(false);
        chainRepository.save(chain);
    }

    // ── MAPPER ─────────────────────────────────────────────────────────────
    private ChainResponseDTO toDTO(Chain chain) {
        ChainResponseDTO dto = new ChainResponseDTO();
        dto.setChainId(chain.getChainId());
        dto.setCompanyName(chain.getCompanyName());
        dto.setGstnNo(chain.getGstnNo());
        dto.setGroupId(chain.getGroup().getGroupId());
        dto.setGroupName(chain.getGroup().getGroupName());
        dto.setIsActive(chain.getIsActive());
        dto.setCreatedAt(chain.getCreatedAt());
        dto.setUpdatedAt(chain.getUpdatedAt());
        return dto;
    }

    public long countActiveChains() {
        return chainRepository.findByIsActiveTrue().size();
    }
}