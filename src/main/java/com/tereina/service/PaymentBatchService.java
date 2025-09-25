package com.tereina.service;

import com.tereina.dto.*;
import com.tereina.entity.Invoice;
import com.tereina.entity.PaymentBatch;
import com.tereina.entity.PaymentRequest;
import com.tereina.repository.InvoiceRepository;
import com.tereina.repository.PaymentBatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaymentBatchService {

  private final PaymentBatchRepository paymentBatchRepository;
  private final InvoiceRepository invoiceRepository;

  @Autowired
  public PaymentBatchService(PaymentBatchRepository paymentBatchRepository,
                             InvoiceRepository invoiceRepository) {
    this.paymentBatchRepository = paymentBatchRepository;
    this.invoiceRepository = invoiceRepository;
  }

  @Transactional(readOnly = true)
  public List<PaymentBatchDto> getAllPaymentBatches() {
    return paymentBatchRepository.findAll()
        .stream()
        .map(PaymentBatchDto::fromEntity)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public Optional<PaymentBatchDto> getPaymentBatchById(Long id) {
    return paymentBatchRepository.findByIdWithPaymentRequests(id)
        .map(PaymentBatchDto::fromEntity);
  }

  public PaymentBatchDto createPaymentBatch(PaymentBatchCreateDto createDto) {
    if (paymentBatchRepository.existsByBatchReference(createDto.getBatchReference())) {
      throw new IllegalArgumentException("Payment batch with reference " + createDto.getBatchReference() + " already exists");
    }

    validatePaymentRequests(createDto.getPaymentRequests());

    PaymentBatch batch = new PaymentBatch(createDto.getBatchReference());

    for (PaymentRequestDto prDto : createDto.getPaymentRequests()) {
      PaymentRequest paymentRequest = new PaymentRequest(
          prDto.getAmount(),
          prDto.getCurrency(),
          prDto.getInvoiceReference(),
          prDto.getDescription()
      );

      Optional<Invoice> invoice = invoiceRepository.findByInvoiceReference(prDto.getInvoiceReference());
      invoice.ifPresent(paymentRequest::setInvoice);

      batch.addPaymentRequest(paymentRequest);
    }

    PaymentBatch savedBatch = paymentBatchRepository.save(batch);
    return PaymentBatchDto.fromEntity(savedBatch);
  }

  public PaymentBatchDto submitPaymentBatch(Long id) {
    PaymentBatch batch = paymentBatchRepository.findByIdWithPaymentRequests(id)
        .orElseThrow(() -> new IllegalArgumentException("Payment batch not found with id: " + id));

    if (!batch.canBeSubmitted()) {
      throw new IllegalStateException("Payment batch cannot be submitted in current state");
    }

    validateBatchBeforeSubmission(batch);
    batch.submit();

    PaymentBatch savedBatch = paymentBatchRepository.save(batch);
    return PaymentBatchDto.fromEntity(savedBatch);
  }

  public ReconciliationResultDto reconcilePaymentBatch(Long id) {
    /**
    TODO
    */
    return null;
  }

  @Transactional(readOnly = true)
  public Optional<ReconciliationResultDto> getReconciliationResult(Long id) {
    PaymentBatch batch = paymentBatchRepository.findByIdWithPaymentRequests(id)
        .orElseThrow(() -> new IllegalArgumentException("Payment batch not found with id: " + id));

    if (batch.getReconciledAt() == null) {
      return Optional.empty();
    }

    return Optional.of(reconcilePaymentBatch(id));
  }

  private void validatePaymentRequests(List<PaymentRequestDto> paymentRequests) {
    for (PaymentRequestDto prDto : paymentRequests) {
      if (!invoiceRepository.existsByInvoiceReference(prDto.getInvoiceReference())) {
        throw new IllegalArgumentException("Invoice not found with reference: " + prDto.getInvoiceReference());
      }
    }
  }

  private void validateBatchBeforeSubmission(PaymentBatch batch) {
    BigDecimal calculatedTotal = batch.getPaymentRequests().stream()
        .map(PaymentRequest::getAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    if (batch.getTotalAmount().compareTo(calculatedTotal) != 0) {
      throw new IllegalStateException("Batch total does not match sum of individual payment requests");
    }
  }

}
