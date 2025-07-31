package com.tereina.controller;

import com.tereina.dto.InvoiceDto;
import com.tereina.service.InvoiceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

  private final InvoiceService invoiceService;

  @Autowired
  public InvoiceController(InvoiceService invoiceService) {
    this.invoiceService = invoiceService;
  }

  @GetMapping
  public ResponseEntity<List<InvoiceDto>> getAllInvoices() {
    List<InvoiceDto> invoices = invoiceService.getAllInvoices();
    return ResponseEntity.ok(invoices);
  }

  @GetMapping("/{id}")
  public ResponseEntity<InvoiceDto> getInvoiceById(@PathVariable Long id) {
    Optional<InvoiceDto> invoice = invoiceService.getInvoiceById(id);
    return invoice.map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<InvoiceDto> createInvoice(@Valid @RequestBody InvoiceDto invoiceDto) {
    try {
      InvoiceDto createdInvoice = invoiceService.createInvoice(invoiceDto);
      return ResponseEntity.status(HttpStatus.CREATED).body(createdInvoice);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<InvoiceDto> updateInvoice(@PathVariable Long id,
                                                  @Valid @RequestBody InvoiceDto invoiceDto) {
    try {
      InvoiceDto updatedInvoice = invoiceService.updateInvoice(id, invoiceDto);
      return ResponseEntity.ok(updatedInvoice);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
    try {
      invoiceService.deleteInvoice(id);
      return ResponseEntity.noContent().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }
}