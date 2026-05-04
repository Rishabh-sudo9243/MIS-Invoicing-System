// src/main/java/com/mis/invoicing/repository/GroupRepository.java

package com.example.MIS.Invoicing.System.repository;

import com.mis.invoicing.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Integer> {

    // Used to check for duplicate name on create
    boolean existsByGroupNameIgnoreCase(String groupName);

    // Used to check duplicate name on update (excluding self)
    boolean existsByGroupNameIgnoreCaseAndGroupIdNot(String groupName, Integer groupId);

    // Fetch only active groups for dashboard listing
    List<Group> findByIsActiveTrue();
}