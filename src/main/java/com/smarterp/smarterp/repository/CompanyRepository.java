package com.smarterp.smarterp.repository;

import com.smarterp.smarterp.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
