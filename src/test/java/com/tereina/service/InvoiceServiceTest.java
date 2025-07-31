package com.tereina.service;

import com.tereina.dto.InvoiceDto;
import com.tereina.entity.Invoice;
import com.tereina.repository.InvoiceRepository;
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
class InvoiceServiceTest {

  @Mock
  private InvoiceRepository invoiceRepository;

  @InjectMocks
  private InvoiceService invoiceService;

  private Invoice testInvoice;
  private InvoiceDto testInvoiceDto;

  @BeforeEach
  void setUp() {
    testInvoice = new Invoice("INV-001", new BigDecimal("1000.00"), "USD", "Test invoice");
    testInvoice.setId(1L);

    testInvoiceDto = new InvoiceDto("INV-001", new BigDecimal("1000.00"), "USD", "Test invoice");
    testInvoiceDto.setId(1L);
  }

  @Test
  void getAllInvoices_Success() {
    when(invoiceRepository.findAll()).thenReturn(Arrays.asList(testInvoice));

    var result = invoiceService.getAllInvoices();

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("INV-001", result.get(0).getInvoiceReference());
  }

  @Test
  void getInvoiceById_Success() {
    when(invoiceRepository.findById(1L)).thenReturn(Optional.of(testInvoice));

    var result = invoiceService.getInvoiceById(1L);

    assertTrue(result.isPresent());
    assertEquals("INV-001", result.get().getInvoiceReference());
  }

  @Test
  void getInvoiceById_NotFound() {
    when(invoiceRepository.findById(1L)).thenReturn(Optional.empty());

    var result = invoiceService.getInvoiceById(1L);

    assertFalse(result.isPresent());
  }

  @Test
  void createInvoice_Success() {
    when(invoiceRepository.existsByInvoiceReference("INV-001")).thenReturn(false);
    when(invoiceRepository.save(any(Invoice.class))).thenReturn(testInvoice);

    var result = invoiceService.createInvoice(testInvoiceDto);

    assertNotNull(result);
    assertEquals("INV-001", result.getInvoiceReference());
    assertEquals(new BigDecimal("1000.00"), result.getAmount());

    verify(invoiceRepository).save(any(Invoice.class));
  }

  @Test
  void createInvoice_DuplicateReference_ThrowsException() {
    when(invoiceRepository.existsByInvoiceReference("INV-001")).thenReturn(true);

    assertThrows(IllegalArgumentException.class,
        () -> invoiceService.createInvoice(testInvoiceDto));
  }

  @Test
  void updateInvoice_Success() {
    InvoiceDto updatedDto = new InvoiceDto("INV-001", new BigDecimal("1500.00"), "USD", "Updated invoice");

    when(invoiceRepository.findById(1L)).thenReturn(Optional.of(testInvoice));
    when(invoiceRepository.save(any(Invoice.class))).thenReturn(testInvoice);

    var result = invoiceService.updateInvoice(1L, updatedDto);

    assertNotNull(result);
    verify(invoiceRepository).save(testInvoice);
  }

  @Test
  void updateInvoice_NotFound_ThrowsException() {
    when(invoiceRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(IllegalArgumentException.class,
        () -> invoiceService.updateInvoice(1L, testInvoiceDto));
  }

  @Test
  void deleteInvoice_Success() {
    when(invoiceRepository.existsById(1L)).thenReturn(true);

    invoiceService.deleteInvoice(1L);

    verify(invoiceRepository).deleteById(1L);
  }

  @Test
  void deleteInvoice_NotFound_ThrowsException() {
    when(invoiceRepository.existsById(1L)).thenReturn(false);

    assertThrows(IllegalArgumentException.class,
        () -> invoiceService.deleteInvoice(1L));
  }
}