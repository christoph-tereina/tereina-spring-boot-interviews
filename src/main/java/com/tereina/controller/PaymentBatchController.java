package com.tereina.controller;

import com.tereina.dto.PaymentBatchCreateDto;
import com.tereina.dto.PaymentBatchDto;
import com.tereina.dto.ReconciliationResultDto;
import com.tereina.service.PaymentBatchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payment-batches")
public class PaymentBatchController {

  private final PaymentBatchService paymentBatchService;

  @Autowired
  public PaymentBatchController(PaymentBatchService paymentBatchService) {
    this.paymentBatchService = paymentBatchService;
  }

  @GetMapping
  public ResponseEntity<List<PaymentBatchDto>> getAllPaymentBatches() {
    List<PaymentBatchDto> batches = paymentBatchService.getAllPaymentBatches();
    return ResponseEntity.ok(batches);
  }

  @GetMapping("/{id}")
  public ResponseEntity<PaymentBatchDto> getPaymentBatchById(@PathVariable Long id) {
    Optional<PaymentBatchDto> batch = paymentBatchService.getPaymentBatchById(id);
    return batch.map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<PaymentBatchDto> createPaymentBatch(@Valid @RequestBody PaymentBatchCreateDto createDto) {
    try {
      PaymentBatchDto createdBatch = paymentBatchService.createPaymentBatch(createDto);
      return ResponseEntity.status(HttpStatus.CREATED).body(createdBatch);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @PutMapping("/{id}/submit")
  public ResponseEntity<PaymentBatchDto> submitPaymentBatch(@PathVariable Long id) {
    try {
      PaymentBatchDto submittedBatch = paymentBatchService.submitPaymentBatch(id);
      return ResponseEntity.ok(submittedBatch);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    } catch (IllegalStateException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @PostMapping("/{id}/reconcile")
  public ResponseEntity<ReconciliationResultDto> reconcilePaymentBatch(@PathVariable Long id) {
    try {
      ReconciliationResultDto result = paymentBatchService.reconcilePaymentBatch(id);
      return ResponseEntity.ok(result);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    } catch (IllegalStateException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping("/{id}/reconciliation-result")
  public ResponseEntity<ReconciliationResultDto> getReconciliationResult(@PathVariable Long id) {
    try {
      Optional<ReconciliationResultDto> result = paymentBatchService.getReconciliationResult(id);
      return result.map(ResponseEntity::ok)
          .orElse(ResponseEntity.notFound().build());
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }
}