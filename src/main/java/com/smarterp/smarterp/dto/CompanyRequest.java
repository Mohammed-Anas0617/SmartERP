package com.smarterp.smarterp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CompanyRequest {

    @NotBlank(message = "Company name is required")
    @Size(max = 150)
    private String name;

    @Size(max = 20)
    private String gstNumber;

    @Size(max = 9, message = "Financial year format should be like '2025-2026'")
    private String financialYear;

    @Size(max = 255)
    private String address;

    @Size(max = 100)
    private String state;

    @Size(max = 20)
    private String contactNumber;
}
