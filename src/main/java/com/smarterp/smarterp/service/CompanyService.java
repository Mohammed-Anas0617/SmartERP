package com.smarterp.smarterp.service;

import com.smarterp.smarterp.dto.CompanyRequest;
import com.smarterp.smarterp.dto.CompanyResponse;
import com.smarterp.smarterp.entity.Company;
import com.smarterp.smarterp.entity.User;
import com.smarterp.smarterp.entity.UserCompany;
import com.smarterp.smarterp.repository.CompanyRepository;
import com.smarterp.smarterp.repository.UserCompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    private static final int MAX_COMPANIES_PER_USER = 5;

    private final CompanyRepository companyRepository;
    private final UserCompanyRepository userCompanyRepository;

    public CompanyService(CompanyRepository companyRepository,
                          UserCompanyRepository userCompanyRepository) {
        this.companyRepository = companyRepository;
        this.userCompanyRepository = userCompanyRepository;
    }

    // CREATE — enforce the 5-company cap, then link the new company to the user as OWNER
    public CompanyResponse createCompany(User currentUser, CompanyRequest request) {

        long existingCount = userCompanyRepository.countByUserIdAndIsActiveTrue(currentUser.getId());
        if (existingCount >= MAX_COMPANIES_PER_USER) {
            throw new IllegalStateException("You can own a maximum of " + MAX_COMPANIES_PER_USER + " companies.");
        }

        Company company = new Company();
        company.setName(request.getName());
        company.setGstNumber(request.getGstNumber());
        company.setFinancialYear(request.getFinancialYear());
        company.setAddress(request.getAddress());
        company.setState(request.getState());
        company.setContactNumber(request.getContactNumber());
        // isActive, createdAt, updatedAt are set automatically via @PrePersist

        Company savedCompany = companyRepository.save(company);

        UserCompany link = new UserCompany();
        link.setUser(currentUser);
        link.setCompany(savedCompany);
        link.setRole("OWNER");
        userCompanyRepository.save(link);

        return toResponse(savedCompany, "OWNER");
    }

    // LIST — only this user's active companies
    public List<CompanyResponse> getMyCompanies(User currentUser) {
        List<UserCompany> links = userCompanyRepository.findByUserIdAndIsActiveTrue(currentUser.getId());

        return links.stream()
                .map(link -> toResponse(link.getCompany(), link.getRole()))
                .toList();
    }

    // UPDATE — only allowed if the company belongs to this user
    public CompanyResponse updateCompany(User currentUser, Long companyId, CompanyRequest request) {

        UserCompany link = userCompanyRepository
                .findByUserIdAndCompanyIdAndIsActiveTrue(currentUser.getId(), companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found or access denied"));

        Company company = link.getCompany();
        company.setName(request.getName());
        company.setGstNumber(request.getGstNumber());
        company.setFinancialYear(request.getFinancialYear());
        company.setAddress(request.getAddress());
        company.setState(request.getState());
        company.setContactNumber(request.getContactNumber());
        // updatedAt is refreshed automatically via @PreUpdate

        Company updated = companyRepository.save(company);
        return toResponse(updated, link.getRole());
    }

    // DELETE — soft delete only: flips is_active to false, never a real DELETE
    public void deleteCompany(User currentUser, Long companyId) {

        UserCompany link = userCompanyRepository
                .findByUserIdAndCompanyIdAndIsActiveTrue(currentUser.getId(), companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found or access denied"));

        Company company = link.getCompany();
        company.setIsActive(false);
        companyRepository.save(company);

        link.setIsActive(false);
        userCompanyRepository.save(link);
    }

    // Manual mapping: entity -> response DTO
    private CompanyResponse toResponse(Company company, String role) {
        return CompanyResponse.builder()
                .id(company.getId())
                .name(company.getName())
                .gstNumber(company.getGstNumber())
                .financialYear(company.getFinancialYear())
                .address(company.getAddress())
                .state(company.getState())
                .contactNumber(company.getContactNumber())
                .role(role)
                .isActive(company.getIsActive())
                .build();
    }
}
