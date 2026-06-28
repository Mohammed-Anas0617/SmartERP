package com.smarterp.smarterp.controller;

import com.smarterp.smarterp.dto.CompanyRequest;
import com.smarterp.smarterp.dto.CompanyResponse;
import com.smarterp.smarterp.entity.User;
import com.smarterp.smarterp.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping
    public ResponseEntity<CompanyResponse> createCompany(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody CompanyRequest request) {

        CompanyResponse response = companyService.createCompany(currentUser, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CompanyResponse>> getMyCompanies(
            @AuthenticationPrincipal User currentUser) {

        return ResponseEntity.ok(companyService.getMyCompanies(currentUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponse> updateCompany(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long id,
            @Valid @RequestBody CompanyRequest request) {

        CompanyResponse response = companyService.updateCompany(currentUser, id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long id) {

        companyService.deleteCompany(currentUser, id);
        return ResponseEntity.noContent().build();
    }
}