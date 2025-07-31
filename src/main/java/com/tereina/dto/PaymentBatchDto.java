package com.tereina.dto;

import com.tereina.entity.PaymentBatch;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PaymentBatchDto {

  private Long id;
  private String batchReference;
  private String status;
  private BigDecimal totalAmount;
  private LocalDateTime createdAt;
  private LocalDateTime submittedAt;
  private LocalDateTime reconciledAt;
  private List<PaymentRequestDto> paymentRequests;

  public PaymentBatchDto() {
  }

  public static PaymentBatchDto fromEntity(PaymentBatch batch) {
    PaymentBatchDto dto = new PaymentBatchDto();
    dto.setId(batch.getId());
    dto.setBatchReference(batch.getBatchReference());
    dto.setStatus(batch.getStatus().name());
    dto.setTotalAmount(batch.getTotalAmount());
    dto.setCreatedAt(batch.getCreatedAt());
    dto.setSubmittedAt(batch.getSubmittedAt());
    dto.setReconciledAt(batch.getReconciledAt());

    if (batch.getPaymentRequests() != null) {
      dto.setPaymentRequests(
          batch.getPaymentRequests().stream()
              .map(pr -> {
                PaymentRequestDto prDto = new PaymentRequestDto();
                prDto.setId(pr.getId());
                prDto.setAmount(pr.getAmount());
                prDto.setCurrency(pr.getCurrency());
                prDto.setInvoiceReference(pr.getInvoiceReference());
                prDto.setDescription(pr.getDescription());
                return prDto;
              })
              .collect(Collectors.toList())
      );
    }

    return dto;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getBatchReference() {
    return batchReference;
  }

  public void setBatchReference(String batchReference) {
    this.batchReference = batchReference;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(BigDecimal totalAmount) {
    this.totalAmount = totalAmount;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getSubmittedAt() {
    return submittedAt;
  }

  public void setSubmittedAt(LocalDateTime submittedAt) {
    this.submittedAt = submittedAt;
  }

  public LocalDateTime getReconciledAt() {
    return reconciledAt;
  }

  public void setReconciledAt(LocalDateTime reconciledAt) {
    this.reconciledAt = reconciledAt;
  }

  public List<PaymentRequestDto> getPaymentRequests() {
    return paymentRequests;
  }

  public void setPaymentRequests(List<PaymentRequestDto> paymentRequests) {
    this.paymentRequests = paymentRequests;
  }
}