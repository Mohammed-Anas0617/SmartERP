package com.smarterp.smarterp.repository;

import com.smarterp.smarterp.entity.Ledger;
import com.smarterp.smarterp.entity.LedgerType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LedgerRepository extends JpaRepository<Ledger, Long> {

    List<Ledger> findByCompanyIdAndIsActiveTrue(Long companyId);

    Optional<Ledger> findByIdAndCompanyIdAndIsActiveTrue(Long id, Long companyId);

    List<Ledger> findByCompanyIdAndTypeAndIsActiveTrue(Long companyId, LedgerType type);

    List<Ledger> findByCompanyIdAndNameContainingIgnoreCaseAndIsActiveTrue(Long companyId, String name);

    long countByCompanyIdAndIsActiveTrue(Long companyId);

    boolean existsByCompanyIdAndNameIgnoreCaseAndIsActiveTrue(Long companyId, String name);
}
