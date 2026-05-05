package com.example.MIS.Invoicing.System.service;

import com.example.MIS.Invoicing.System.dto.GroupRequestDTO;
import com.example.MIS.Invoicing.System.dto.GroupResponseDTO;
import com.example.MIS.Invoicing.System.entity.Group;
import com.example.MIS.Invoicing.System.repository.ChainRepository;
import com.example.MIS.Invoicing.System.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;

    // ── GET ALL ACTIVE GROUPS ──────────────────────────────────────────────
    public List<GroupResponseDTO> getAllActiveGroups() {
        return groupRepository.findByIsActiveTrue()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ── ADD GROUP ──────────────────────────────────────────────────────────
    public GroupResponseDTO addGroup(GroupRequestDTO dto) {
        String name = dto.getGroupName().trim();

        if (groupRepository.existsByGroupNameIgnoreCase(name)) {
            throw new RuntimeException("Group Already Exists!!!");
        }

        Group group = new Group();
        group.setGroupName(name);
        group.setIsActive(true);

        return toDTO(groupRepository.save(group));
    }

    // ── UPDATE GROUP NAME ──────────────────────────────────────────────────
    public GroupResponseDTO updateGroup(Integer groupId, GroupRequestDTO dto) {
        String name = dto.getGroupName().trim();

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found with id: " + groupId));

        if (groupRepository.existsByGroupNameIgnoreCaseAndGroupIdNot(name, groupId)) {
            throw new RuntimeException("Group name already taken by another group");
        }

        group.setGroupName(name);
        return toDTO(groupRepository.save(group));
    }

    // In GroupService.java — inject ChainRepository and update softDeleteGroup

// Add this field at the top (alongside groupRepository):
private final ChainRepository chainRepository;

// Replace the softDeleteGroup method:
public void softDeleteGroup(Integer groupId) {
    Group group = groupRepository.findById(groupId)
            .orElseThrow(() -> new RuntimeException("Group not found with id: " + groupId));

    // Block delete if any active chain is linked to this group
    boolean linkedToChain = chainRepository.existsByGroup_GroupIdAndIsActiveTrue(groupId);
    if (linkedToChain) {
        throw new RuntimeException("Cannot delete group: it is linked to one or more active chains");
    }

    group.setIsActive(false);
    groupRepository.save(group);
}

    // ── MAPPER ────────────────────────────────────────────────────────────
    private GroupResponseDTO toDTO(Group group) {
        GroupResponseDTO dto = new GroupResponseDTO();
        dto.setGroupId(group.getGroupId());
        dto.setGroupName(group.getGroupName());
        dto.setIsActive(group.getIsActive());
        dto.setCreatedAt(group.getCreatedAt());
        dto.setUpdatedAt(group.getUpdatedAt());
        return dto;
    }
}