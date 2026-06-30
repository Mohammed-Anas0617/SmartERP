package com.smarterp.smarterp.service;

import com.smarterp.smarterp.dto.GroupRequestDto;
import com.smarterp.smarterp.dto.GroupResponseDto;
import com.smarterp.smarterp.entity.Company;
import com.smarterp.smarterp.entity.Group;
import com.smarterp.smarterp.repository.CompanyRepository;
import com.smarterp.smarterp.repository.GroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final CompanyRepository companyRepository;

    public GroupService(GroupRepository groupRepository, CompanyRepository companyRepository) {
        this.groupRepository = groupRepository;
        this.companyRepository = companyRepository;
    }

    public GroupResponseDto createGroup(Long companyId, GroupRequestDto request) {
        if (groupRepository.existsByCompanyIdAndNameIgnoreCaseAndIsActiveTrue(companyId, request.getName())) {
            throw new IllegalArgumentException("A group with this name already exists in this company");
        }

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found"));

        Group group = new Group();
        group.setCompany(company);
        group.setName(request.getName());
        group.setNature(request.getNature());
        group.setActive(true);

        Group saved = groupRepository.save(group);
        return toResponseDto(saved);
    }

    public List<GroupResponseDto> getAllGroups(Long companyId) {
        return groupRepository.findByCompanyIdAndIsActiveTrue(companyId)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public GroupResponseDto getGroupById(Long companyId, Long groupId) {
        Group group = groupRepository.findByIdAndCompanyIdAndIsActiveTrue(groupId, companyId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));
        return toResponseDto(group);
    }

    public GroupResponseDto updateGroup(Long companyId, Long groupId, GroupRequestDto request) {
        Group group = groupRepository.findByIdAndCompanyIdAndIsActiveTrue(groupId, companyId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        if (!group.getName().equalsIgnoreCase(request.getName())
                && groupRepository.existsByCompanyIdAndNameIgnoreCaseAndIsActiveTrue(companyId, request.getName())) {
            throw new IllegalArgumentException("A group with this name already exists in this company");
        }

        group.setName(request.getName());
        group.setNature(request.getNature());

        Group updated = groupRepository.save(group);
        return toResponseDto(updated);
    }

    public void deleteGroup(Long companyId, Long groupId) {
        Group group = groupRepository.findByIdAndCompanyIdAndIsActiveTrue(groupId, companyId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));
        group.setActive(false);
        groupRepository.save(group);
    }

    public long countGroups(Long companyId) {
        return groupRepository.countByCompanyIdAndIsActiveTrue(companyId);
    }

    /**
     * Used by LedgerService to resolve a groupId into an actual Group entity
     * when creating/updating a ledger.
     */
    public Group getGroupEntityById(Long companyId, Long groupId) {
        return groupRepository.findByIdAndCompanyIdAndIsActiveTrue(groupId, companyId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));
    }

    private GroupResponseDto toResponseDto(Group group) {
        return GroupResponseDto.builder()
                .id(group.getId())
                .name(group.getName())
                .nature(group.getNature())
                .isActive(group.isActive())
                .createdAt(group.getCreatedAt())
                .updatedAt(group.getUpdatedAt())
                .build();
    }
}