package com.tereina.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class PaymentBatchCreateDto {

  @NotBlank(message = "Batch reference is required")
  private String batchReference;

  @NotEmpty(message = "Payment requests cannot be empty")
  @Valid
  private List<PaymentRequestDto> paymentRequests;

  public PaymentBatchCreateDto() {
  }

  public PaymentBatchCreateDto(String batchReference, List<PaymentRequestDto> paymentRequests) {
    this.batchReference = batchReference;
    this.paymentRequests = paymentRequests;
  }

  public String getBatchReference() {
    return batchReference;
  }

  public void setBatchReference(String batchReference) {
    this.batchReference = batchReference;
  }

  public List<PaymentRequestDto> getPaymentRequests() {
    return paymentRequests;
  }

  public void setPaymentRequests(List<PaymentRequestDto> paymentRequests) {
    this.paymentRequests = paymentRequests;
  }
}