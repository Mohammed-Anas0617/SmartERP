package com.smarterp.smarterp.dto;

public class DashboardResponse {

    private Long companyId;
    private String companyName;
    private String gstNumber;
    private long ledgerCount;
    private long stockItemCount;
    private long voucherCount;

    public DashboardResponse(Long companyId, String companyName, String gstNumber,
                             long ledgerCount, long stockItemCount, long voucherCount) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.gstNumber = gstNumber;
        this.ledgerCount = ledgerCount;
        this.stockItemCount = stockItemCount;
        this.voucherCount = voucherCount;
    }

    public Long getCompanyId() { return companyId; }
    public String getCompanyName() { return companyName; }
    public String getGstNumber() { return gstNumber; }
    public long getLedgerCount() { return ledgerCount; }
    public long getStockItemCount() { return stockItemCount; }
    public long getVoucherCount() { return voucherCount; }
}
