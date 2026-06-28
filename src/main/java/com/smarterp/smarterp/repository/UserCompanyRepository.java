package com.smarterp.smarterp.repository;

import com.smarterp.smarterp.entity.UserCompany;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserCompanyRepository extends JpaRepository<UserCompany, Long> {

    // All active company links for a given user (used to list their companies)
    List<UserCompany> findByUserIdAndIsActiveTrue(Long userId);

    // Count active companies a user owns (used to enforce the 5-company cap)
    long countByUserIdAndIsActiveTrue(Long userId);

    // Find a specific user-company link (used for update/delete, to verify ownership)
    Optional<UserCompany> findByUserIdAndCompanyIdAndIsActiveTrue(Long userId, Long companyId);
}
