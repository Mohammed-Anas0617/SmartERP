package com.smarterp.smarterp.controller;

import com.smarterp.smarterp.dto.DashboardResponse;
import com.smarterp.smarterp.entity.Company;
import com.smarterp.smarterp.repository.CompanyRepository;
import com.smarterp.smarterp.security.CompanyContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardController {

    private final CompanyRepository companyRepository;

    public DashboardController(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @GetMapping("/api/dashboard")
    public DashboardResponse getDashboard() {

        Long companyId = CompanyContextHolder.getCompanyId();

        if (companyId == null) {
            throw new IllegalStateException("No active company selected. Pass X-Company-Id header.");
        }

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalStateException("Company not found"));

        return new DashboardResponse(
                company.getId(),
                company.getName(),
                company.getGstNumber(),
                0L,  // ledgerCount - wired up in Day 6
                0L,  // stockItemCount - wired up in Day 8
                0L   // voucherCount - wired up in Day 9/10
        );
    }
}
