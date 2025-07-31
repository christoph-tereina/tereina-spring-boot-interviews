package com.tereina.dto;

import java.util.List;

public class ReconciliationResultDto {

  private Long batchId;
  private String overallStatus;
  private int totalRequests;
  private int reconciledCount;
  private int discrepancyCount;
  private List<ReconciliationItemDto> reconciliationItems;

  public ReconciliationResultDto() {
  }

  public ReconciliationResultDto(Long batchId, String overallStatus, int totalRequests,
                                 int reconciledCount, int discrepancyCount,
                                 List<ReconciliationItemDto> reconciliationItems) {
    this.batchId = batchId;
    this.overallStatus = overallStatus;
    this.totalRequests = totalRequests;
    this.reconciledCount = reconciledCount;
    this.discrepancyCount = discrepancyCount;
    this.reconciliationItems = reconciliationItems;
  }

  public Long getBatchId() {
    return batchId;
  }

  public void setBatchId(Long batchId) {
    this.batchId = batchId;
  }

  public String getOverallStatus() {
    return overallStatus;
  }

  public void setOverallStatus(String overallStatus) {
    this.overallStatus = overallStatus;
  }

  public int getTotalRequests() {
    return totalRequests;
  }

  public void setTotalRequests(int totalRequests) {
    this.totalRequests = totalRequests;
  }

  public int getReconciledCount() {
    return reconciledCount;
  }

  public void setReconciledCount(int reconciledCount) {
    this.reconciledCount = reconciledCount;
  }

  public int getDiscrepancyCount() {
    return discrepancyCount;
  }

  public void setDiscrepancyCount(int discrepancyCount) {
    this.discrepancyCount = discrepancyCount;
  }

  public List<ReconciliationItemDto> getReconciliationItems() {
    return reconciliationItems;
  }

  public void setReconciliationItems(List<ReconciliationItemDto> reconciliationItems) {
    this.reconciliationItems = reconciliationItems;
  }
}