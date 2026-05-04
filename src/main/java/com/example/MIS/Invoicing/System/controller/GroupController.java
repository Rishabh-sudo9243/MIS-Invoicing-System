package com.example.MIS.Invoicing.System.controller;

import com.example.MIS.Invoicing.System.dto.GroupRequestDTO;
import com.example.MIS.Invoicing.System.dto.GroupResponseDTO;
import com.example.MIS.Invoicing.System.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    // GET /api/groups  → list all active groups
    @GetMapping
    public ResponseEntity<List<GroupResponseDTO>> getAllGroups() {
        return ResponseEntity.ok(groupService.getAllActiveGroups());
    }

    // POST /api/groups  → create new group
    @PostMapping
    public ResponseEntity<?> addGroup(@Valid @RequestBody GroupRequestDTO dto) {
        try {
            GroupResponseDTO created = groupService.addGroup(dto);
            return ResponseEntity.ok(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT /api/groups/{id}  → update group name
    @PutMapping("/{id}")
    public ResponseEntity<?> updateGroup(
            @PathVariable Integer id,
            @Valid @RequestBody GroupRequestDTO dto) {
        try {
            GroupResponseDTO updated = groupService.updateGroup(id, dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE /api/groups/{id}  → soft delete (sets is_active = false)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGroup(@PathVariable Integer id) {
        try {
            groupService.softDeleteGroup(id);
            return ResponseEntity.ok("Group deactivated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}