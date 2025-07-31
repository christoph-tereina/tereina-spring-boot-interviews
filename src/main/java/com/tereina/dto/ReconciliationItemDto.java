package com.tereina.dto;

import java.math.BigDecimal;

public class ReconciliationItemDto {

  private Long paymentRequestId;
  private Long invoiceId;
  private String status;
  private BigDecimal paymentAmount;
  private BigDecimal invoiceAmount;
  private String discrepancy;

  public ReconciliationItemDto() {
  }

  public ReconciliationItemDto(Long paymentRequestId, Long invoiceId, String status,
                               BigDecimal paymentAmount, BigDecimal invoiceAmount, String discrepancy) {
    this.paymentRequestId = paymentRequestId;
    this.invoiceId = invoiceId;
    this.status = status;
    this.paymentAmount = paymentAmount;
    this.invoiceAmount = invoiceAmount;
    this.discrepancy = discrepancy;
  }

  public Long getPaymentRequestId() {
    return paymentRequestId;
  }

  public void setPaymentRequestId(Long paymentRequestId) {
    this.paymentRequestId = paymentRequestId;
  }

  public Long getInvoiceId() {
    return invoiceId;
  }

  public void setInvoiceId(Long invoiceId) {
    this.invoiceId = invoiceId;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public BigDecimal getPaymentAmount() {
    return paymentAmount;
  }

  public void setPaymentAmount(BigDecimal paymentAmount) {
    this.paymentAmount = paymentAmount;
  }

  public BigDecimal getInvoiceAmount() {
    return invoiceAmount;
  }

  public void setInvoiceAmount(BigDecimal invoiceAmount) {
    this.invoiceAmount = invoiceAmount;
  }

  public String getDiscrepancy() {
    return discrepancy;
  }

  public void setDiscrepancy(String discrepancy) {
    this.discrepancy = discrepancy;
  }
}