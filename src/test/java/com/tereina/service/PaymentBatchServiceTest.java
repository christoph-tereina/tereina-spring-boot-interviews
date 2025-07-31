package com.tereina.service;

import com.tereina.dto.PaymentBatchCreateDto;
import com.tereina.dto.PaymentRequestDto;
import com.tereina.entity.Invoice;
import com.tereina.entity.PaymentBatch;
import com.tereina.repository.InvoiceRepository;
import com.tereina.repository.PaymentBatchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentBatchServiceTest {

  @Mock
  private PaymentBatchRepository paymentBatchRepository;

  @Mock
  private InvoiceRepository invoiceRepository;

  @InjectMocks
  private PaymentBatchService paymentBatchService;

  private Invoice testInvoice;
  private PaymentBatch testBatch;

  @BeforeEach
  void setUp() {
    testInvoice = new Invoice("INV-001", new BigDecimal("1000.00"), "USD", "Test invoice");
    testInvoice.setId(1L);

    testBatch = new PaymentBatch("BATCH-001");
    testBatch.setId(1L);
    testBatch.setStatus(PaymentBatch.BatchStatus.CREATED);
  }

  @Test
  void createPaymentBatch_Success() {
    PaymentRequestDto paymentRequest = new PaymentRequestDto(
        new BigDecimal("1000.00"), "USD", "INV-001", "Payment for services"
    );

    PaymentBatchCreateDto createDto = new PaymentBatchCreateDto(
        "BATCH-001", Arrays.asList(paymentRequest)
    );

    when(paymentBatchRepository.existsByBatchReference("BATCH-001")).thenReturn(false);
    when(invoiceRepository.existsByInvoiceReference("INV-001")).thenReturn(true);
    when(invoiceRepository.findByInvoiceReference("INV-001")).thenReturn(Optional.of(testInvoice));
    when(paymentBatchRepository.save(any(PaymentBatch.class))).thenReturn(testBatch);

    var result = paymentBatchService.createPaymentBatch(createDto);

    assertNotNull(result);
    assertEquals("BATCH-001", result.getBatchReference());
    assertEquals("CREATED", result.getStatus());

    verify(paymentBatchRepository).save(any(PaymentBatch.class));
  }

  @Test
  void createPaymentBatch_DuplicateReference_ThrowsException() {
    PaymentRequestDto paymentRequest = new PaymentRequestDto(
        new BigDecimal("1000.00"), "USD", "INV-001", "Payment for services"
    );

    PaymentBatchCreateDto createDto = new PaymentBatchCreateDto(
        "BATCH-001", Arrays.asList(paymentRequest)
    );

    when(paymentBatchRepository.existsByBatchReference("BATCH-001")).thenReturn(true);

    assertThrows(IllegalArgumentException.class,
        () -> paymentBatchService.createPaymentBatch(createDto));
  }

  @Test
  void createPaymentBatch_InvalidInvoiceReference_ThrowsException() {
    PaymentRequestDto paymentRequest = new PaymentRequestDto(
        new BigDecimal("1000.00"), "USD", "INVALID-REF", "Payment for services"
    );

    PaymentBatchCreateDto createDto = new PaymentBatchCreateDto(
        "BATCH-001", Arrays.asList(paymentRequest)
    );

    when(paymentBatchRepository.existsByBatchReference("BATCH-001")).thenReturn(false);
    when(invoiceRepository.existsByInvoiceReference("INVALID-REF")).thenReturn(false);

    assertThrows(IllegalArgumentException.class,
        () -> paymentBatchService.createPaymentBatch(createDto));
  }

  @Test
  void submitPaymentBatch_Success() {
    testBatch.setStatus(PaymentBatch.BatchStatus.CREATED);
    testBatch.setTotalAmount(new BigDecimal("1000.00"));

    com.tereina.entity.PaymentRequest paymentRequest = new com.tereina.entity.PaymentRequest(
        new BigDecimal("1000.00"), "USD", "INV-001", "Test payment"
    );
    testBatch.addPaymentRequest(paymentRequest);

    when(paymentBatchRepository.findByIdWithPaymentRequests(1L)).thenReturn(Optional.of(testBatch));
    when(paymentBatchRepository.save(any(PaymentBatch.class))).thenReturn(testBatch);

    var result = paymentBatchService.submitPaymentBatch(1L);

    assertNotNull(result);
    assertEquals("SUBMITTED", result.getStatus());

    verify(paymentBatchRepository).save(testBatch);
  }

  @Test
  void submitPaymentBatch_BatchNotFound_ThrowsException() {
    when(paymentBatchRepository.findByIdWithPaymentRequests(1L)).thenReturn(Optional.empty());

    assertThrows(IllegalArgumentException.class,
        () -> paymentBatchService.submitPaymentBatch(1L));
  }

  @Test
  void submitPaymentBatch_WrongStatus_ThrowsException() {
    testBatch.setStatus(PaymentBatch.BatchStatus.SUBMITTED);

    when(paymentBatchRepository.findByIdWithPaymentRequests(1L)).thenReturn(Optional.of(testBatch));

    assertThrows(IllegalStateException.class,
        () -> paymentBatchService.submitPaymentBatch(1L));
  }

  @Test
  void reconcilePaymentBatch_NotSubmitted_ThrowsException() {
    testBatch.setStatus(PaymentBatch.BatchStatus.CREATED);

    when(paymentBatchRepository.findByIdWithPaymentRequests(1L)).thenReturn(Optional.of(testBatch));

    assertThrows(IllegalStateException.class,
        () -> paymentBatchService.reconcilePaymentBatch(1L));
  }
}