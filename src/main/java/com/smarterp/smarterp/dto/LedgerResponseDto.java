package com.smarterp.smarterp.dto;

import com.smarterp.smarterp.entity.LedgerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LedgerResponseDto {

    private Long id;
    private String name;
    private LedgerType type;
    private BigDecimal openingBalance;
    private BigDecimal currentBalance;
    private String gstNumber;
    private String address;
    private String contactNumber;
    private boolean isActive;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}