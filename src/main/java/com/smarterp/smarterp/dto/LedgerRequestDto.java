package com.smarterp.smarterp.dto;

import com.smarterp.smarterp.entity.LedgerType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LedgerRequestDto {

    @NotBlank(message = "Ledger name is required")
    private String name;

    @NotNull(message = "Ledger type is required")
    private LedgerType type;

    private BigDecimal openingBalance = BigDecimal.ZERO;

    private String gstNumber;

    private String address;

    private String contactNumber;

    // Optional - existing ledgers can stay ungrouped for now
    private Long groupId;
}