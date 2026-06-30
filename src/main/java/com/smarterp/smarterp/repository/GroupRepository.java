package com.smarterp.smarterp.repository;

import com.smarterp.smarterp.entity.Group;
import com.smarterp.smarterp.entity.GroupNature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {

    List<Group> findByCompanyIdAndIsActiveTrue(Long companyId);

    Optional<Group> findByIdAndCompanyIdAndIsActiveTrue(Long id, Long companyId);

    List<Group> findByCompanyIdAndNatureAndIsActiveTrue(Long companyId, GroupNature nature);

    boolean existsByCompanyIdAndNameIgnoreCaseAndIsActiveTrue(Long companyId, String name);

    long countByCompanyIdAndIsActiveTrue(Long companyId);
}