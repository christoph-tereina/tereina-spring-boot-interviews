package com.tereina.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "payment_batches")
public class PaymentBatch {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Column(unique = true)
  private String batchReference;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private BatchStatus status = BatchStatus.CREATED;

  @Column(precision = 19, scale = 2)
  private BigDecimal totalAmount = BigDecimal.ZERO;

  @Column(nullable = false)
  private LocalDateTime createdAt = LocalDateTime.now();

  private LocalDateTime submittedAt;

  private LocalDateTime reconciledAt;

  @OneToMany(mappedBy = "paymentBatch", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<PaymentRequest> paymentRequests = new ArrayList<>();

  public PaymentBatch() {
  }

  public PaymentBatch(String batchReference) {
    this.batchReference = batchReference;
  }

  public void addPaymentRequest(PaymentRequest paymentRequest) {
    paymentRequests.add(paymentRequest);
    paymentRequest.setPaymentBatch(this);
    recalculateTotal();
  }

  public void removePaymentRequest(PaymentRequest paymentRequest) {
    paymentRequests.remove(paymentRequest);
    paymentRequest.setPaymentBatch(null);
    recalculateTotal();
  }

  private void recalculateTotal() {
    this.totalAmount = paymentRequests.stream()
        .map(PaymentRequest::getAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public boolean canBeSubmitted() {
    return status == BatchStatus.CREATED && !paymentRequests.isEmpty();
  }

  public void submit() {
    if (!canBeSubmitted()) {
      throw new IllegalStateException("Batch cannot be submitted in current state");
    }
    this.status = BatchStatus.SUBMITTED;
    this.submittedAt = LocalDateTime.now();
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

  public BatchStatus getStatus() {
    return status;
  }

  public void setStatus(BatchStatus status) {
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

  public List<PaymentRequest> getPaymentRequests() {
    return paymentRequests;
  }

  public void setPaymentRequests(List<PaymentRequest> paymentRequests) {
    this.paymentRequests = paymentRequests;
  }

  public enum BatchStatus {
    CREATED, SUBMITTED, RECONCILED, PARTIALLY_RECONCILED, FAILED
  }
}