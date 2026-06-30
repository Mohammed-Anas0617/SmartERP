package com.smarterp.smarterp.service;

import com.smarterp.smarterp.dto.LedgerRequestDto;
import com.smarterp.smarterp.dto.LedgerResponseDto;
import com.smarterp.smarterp.entity.Company;
import com.smarterp.smarterp.entity.Group;
import com.smarterp.smarterp.entity.Ledger;
import com.smarterp.smarterp.repository.CompanyRepository;
import com.smarterp.smarterp.repository.LedgerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LedgerService {

    private final LedgerRepository ledgerRepository;
    private final CompanyRepository companyRepository;
    private final GroupService groupService;

    public LedgerService(LedgerRepository ledgerRepository,
                         CompanyRepository companyRepository,
                         GroupService groupService) {
        this.ledgerRepository = ledgerRepository;
        this.companyRepository = companyRepository;
        this.groupService = groupService;
    }

    public LedgerResponseDto createLedger(Long companyId, LedgerRequestDto request) {
        if (ledgerRepository.existsByCompanyIdAndNameIgnoreCaseAndIsActiveTrue(companyId, request.getName())) {
            throw new IllegalArgumentException("A ledger with this name already exists in this company");
        }

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found"));

        Ledger ledger = new Ledger();
        ledger.setCompany(company);
        ledger.setName(request.getName());
        ledger.setType(request.getType());
        ledger.setOpeningBalance(request.getOpeningBalance());
        ledger.setCurrentBalance(request.getOpeningBalance());
        ledger.setGstNumber(request.getGstNumber());
        ledger.setAddress(request.getAddress());
        ledger.setContactNumber(request.getContactNumber());
        ledger.setActive(true);

        // Resolve and attach group, if provided
        if (request.getGroupId() != null) {
            Group group = groupService.getGroupEntityById(companyId, request.getGroupId());
            ledger.setGroup(group);
        }

        Ledger saved = ledgerRepository.save(ledger);
        return toResponseDto(saved);
    }

    public List<LedgerResponseDto> getAllLedgers(Long companyId) {
        return ledgerRepository.findByCompanyIdAndIsActiveTrue(companyId)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public LedgerResponseDto getLedgerById(Long companyId, Long ledgerId) {
        Ledger ledger = ledgerRepository.findByIdAndCompanyIdAndIsActiveTrue(ledgerId, companyId)
                .orElseThrow(() -> new IllegalArgumentException("Ledger not found"));
        return toResponseDto(ledger);
    }

    public LedgerResponseDto updateLedger(Long companyId, Long ledgerId, LedgerRequestDto request) {
        Ledger ledger = ledgerRepository.findByIdAndCompanyIdAndIsActiveTrue(ledgerId, companyId)
                .orElseThrow(() -> new IllegalArgumentException("Ledger not found"));

        if (!ledger.getName().equalsIgnoreCase(request.getName())
                && ledgerRepository.existsByCompanyIdAndNameIgnoreCaseAndIsActiveTrue(companyId, request.getName())) {
            throw new IllegalArgumentException("A ledger with this name already exists in this company");
        }

        ledger.setName(request.getName());
        ledger.setType(request.getType());
        ledger.setGstNumber(request.getGstNumber());
        ledger.setAddress(request.getAddress());
        ledger.setContactNumber(request.getContactNumber());
        // Note: openingBalance and currentBalance are intentionally NOT updated here.
        // Changing opening balance after vouchers exist would corrupt accounting history.

        // Only touch the group if groupId is explicitly provided.
        // Omitting groupId from the request leaves the existing assignment unchanged,
        // so partial updates (e.g. just editing a phone number) don't accidentally
        // wipe out group classification needed for Balance Sheet/P&L reports later.
        if (request.getGroupId() != null) {
            Group group = groupService.getGroupEntityById(companyId, request.getGroupId());
            ledger.setGroup(group);
        }

        Ledger updated = ledgerRepository.save(ledger);
        return toResponseDto(updated);
    }

    public void deleteLedger(Long companyId, Long ledgerId) {
        Ledger ledger = ledgerRepository.findByIdAndCompanyIdAndIsActiveTrue(ledgerId, companyId)
                .orElseThrow(() -> new IllegalArgumentException("Ledger not found"));
        ledger.setActive(false);
        ledgerRepository.save(ledger);
    }

    public long countLedgers(Long companyId) {
        return ledgerRepository.countByCompanyIdAndIsActiveTrue(companyId);
    }

    private LedgerResponseDto toResponseDto(Ledger ledger) {
        LedgerResponseDto.LedgerResponseDtoBuilder builder = LedgerResponseDto.builder()
                .id(ledger.getId())
                .name(ledger.getName())
                .type(ledger.getType())
                .openingBalance(ledger.getOpeningBalance())
                .currentBalance(ledger.getCurrentBalance())
                .gstNumber(ledger.getGstNumber())
                .address(ledger.getAddress())
                .contactNumber(ledger.getContactNumber())
                .isActive(ledger.isActive())
                .createdAt(ledger.getCreatedAt())
                .updatedAt(ledger.getUpdatedAt());

        if (ledger.getGroup() != null) {
            builder.groupId(ledger.getGroup().getId())
                    .groupName(ledger.getGroup().getName());
        }

        return builder.build();
    }
}