package com.smarterp.smarterp.controller;

import com.smarterp.smarterp.dto.LedgerRequestDto;
import com.smarterp.smarterp.dto.LedgerResponseDto;
import com.smarterp.smarterp.security.CompanyContextHolder;
import com.smarterp.smarterp.service.LedgerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ledgers")
public class LedgerController {

    private final LedgerService ledgerService;

    public LedgerController(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    @PostMapping
    public ResponseEntity<LedgerResponseDto> createLedger(@Valid @RequestBody LedgerRequestDto request) {
        Long companyId = CompanyContextHolder.getCompanyId();
        LedgerResponseDto response = ledgerService.createLedger(companyId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<LedgerResponseDto>> getAllLedgers() {
        Long companyId = CompanyContextHolder.getCompanyId();
        return ResponseEntity.ok(ledgerService.getAllLedgers(companyId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LedgerResponseDto> getLedgerById(@PathVariable Long id) {
        Long companyId = CompanyContextHolder.getCompanyId();
        return ResponseEntity.ok(ledgerService.getLedgerById(companyId, id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LedgerResponseDto> updateLedger(
            @PathVariable Long id,
            @Valid @RequestBody LedgerRequestDto request) {
        Long companyId = CompanyContextHolder.getCompanyId();
        return ResponseEntity.ok(ledgerService.updateLedger(companyId, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLedger(@PathVariable Long id) {
        Long companyId = CompanyContextHolder.getCompanyId();
        ledgerService.deleteLedger(companyId, id);
        return ResponseEntity.noContent().build();
    }
}