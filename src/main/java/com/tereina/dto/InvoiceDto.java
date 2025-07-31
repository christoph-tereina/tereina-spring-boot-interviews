package com.tereina.dto;

import com.tereina.entity.Invoice;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InvoiceDto {

  private Long id;

  @NotBlank(message = "Invoice reference is required")
  private String invoiceReference;

  @NotNull(message = "Amount is required")
  @Positive(message = "Amount must be positive")
  private BigDecimal amount;

  @NotBlank(message = "Currency is required")
  private String currency;

  private String description;
  private String status;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public InvoiceDto() {
  }

  public InvoiceDto(String invoiceReference, BigDecimal amount, String currency, String description) {
    this.invoiceReference = invoiceReference;
    this.amount = amount;
    this.currency = currency;
    this.description = description;
  }

  public static InvoiceDto fromEntity(Invoice invoice) {
    InvoiceDto dto = new InvoiceDto();
    dto.setId(invoice.getId());
    dto.setInvoiceReference(invoice.getInvoiceReference());
    dto.setAmount(invoice.getAmount());
    dto.setCurrency(invoice.getCurrency());
    dto.setDescription(invoice.getDescription());
    dto.setStatus(invoice.getStatus().name());
    dto.setCreatedAt(invoice.getCreatedAt());
    dto.setUpdatedAt(invoice.getUpdatedAt());
    return dto;
  }

  public Invoice toEntity() {
    Invoice invoice = new Invoice();
    invoice.setId(this.id);
    invoice.setInvoiceReference(this.invoiceReference);
    invoice.setAmount(this.amount);
    invoice.setCurrency(this.currency);
    invoice.setDescription(this.description);
    if (this.status != null) {
      invoice.setStatus(Invoice.InvoiceStatus.valueOf(this.status));
    }
    return invoice;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getInvoiceReference() {
    return invoiceReference;
  }

  public void setInvoiceReference(String invoiceReference) {
    this.invoiceReference = invoiceReference;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }
}