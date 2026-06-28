package com.smarterp.smarterp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyResponse {

    private Long id;
    private String name;
    private String gstNumber;
    private String financialYear;
    private String address;
    private String state;
    private String contactNumber;
    private String role;       // OWNER, etc — comes from user_companies, not companies
    private Boolean isActive;
}
