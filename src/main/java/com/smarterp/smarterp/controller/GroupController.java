package com.smarterp.smarterp.controller;

import com.smarterp.smarterp.dto.GroupRequestDto;
import com.smarterp.smarterp.dto.GroupResponseDto;
import com.smarterp.smarterp.security.CompanyContextHolder;
import com.smarterp.smarterp.service.GroupService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping
    public ResponseEntity<GroupResponseDto> createGroup(@Valid @RequestBody GroupRequestDto request) {
        Long companyId = CompanyContextHolder.getCompanyId();
        GroupResponseDto response = groupService.createGroup(companyId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<GroupResponseDto>> getAllGroups() {
        Long companyId = CompanyContextHolder.getCompanyId();
        return ResponseEntity.ok(groupService.getAllGroups(companyId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupResponseDto> getGroupById(@PathVariable Long id) {
        Long companyId = CompanyContextHolder.getCompanyId();
        return ResponseEntity.ok(groupService.getGroupById(companyId, id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupResponseDto> updateGroup(
            @PathVariable Long id,
            @Valid @RequestBody GroupRequestDto request) {
        Long companyId = CompanyContextHolder.getCompanyId();
        return ResponseEntity.ok(groupService.updateGroup(companyId, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        Long companyId = CompanyContextHolder.getCompanyId();
        groupService.deleteGroup(companyId, id);
        return ResponseEntity.noContent().build();
    }
}