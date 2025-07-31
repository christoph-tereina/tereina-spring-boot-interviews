package com.tereina.service;

import com.tereina.dto.InvoiceDto;
import com.tereina.entity.Invoice;
import com.tereina.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class InvoiceService {

  private final InvoiceRepository invoiceRepository;

  @Autowired
  public InvoiceService(InvoiceRepository invoiceRepository) {
    this.invoiceRepository = invoiceRepository;
  }

  @Transactional(readOnly = true)
  public List<InvoiceDto> getAllInvoices() {
    return invoiceRepository.findAll()
        .stream()
        .map(InvoiceDto::fromEntity)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public Optional<InvoiceDto> getInvoiceById(Long id) {
    return invoiceRepository.findById(id)
        .map(InvoiceDto::fromEntity);
  }

  @Transactional(readOnly = true)
  public Optional<InvoiceDto> getInvoiceByReference(String reference) {
    return invoiceRepository.findByInvoiceReference(reference)
        .map(InvoiceDto::fromEntity);
  }

  public InvoiceDto createInvoice(InvoiceDto invoiceDto) {
    if (invoiceRepository.existsByInvoiceReference(invoiceDto.getInvoiceReference())) {
      throw new IllegalArgumentException("Invoice with reference " + invoiceDto.getInvoiceReference() + " already exists");
    }

    Invoice invoice = invoiceDto.toEntity();
    Invoice savedInvoice = invoiceRepository.save(invoice);
    return InvoiceDto.fromEntity(savedInvoice);
  }

  public InvoiceDto updateInvoice(Long id, InvoiceDto invoiceDto) {
    Invoice existingInvoice = invoiceRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Invoice not found with id: " + id));

    if (!existingInvoice.getInvoiceReference().equals(invoiceDto.getInvoiceReference()) &&
        invoiceRepository.existsByInvoiceReference(invoiceDto.getInvoiceReference())) {
      throw new IllegalArgumentException("Invoice with reference " + invoiceDto.getInvoiceReference() + " already exists");
    }

    existingInvoice.setInvoiceReference(invoiceDto.getInvoiceReference());
    existingInvoice.setAmount(invoiceDto.getAmount());
    existingInvoice.setCurrency(invoiceDto.getCurrency());
    existingInvoice.setDescription(invoiceDto.getDescription());

    if (invoiceDto.getStatus() != null) {
      existingInvoice.setStatus(Invoice.InvoiceStatus.valueOf(invoiceDto.getStatus()));
    }

    Invoice savedInvoice = invoiceRepository.save(existingInvoice);
    return InvoiceDto.fromEntity(savedInvoice);
  }

  public void deleteInvoice(Long id) {
    if (!invoiceRepository.existsById(id)) {
      throw new IllegalArgumentException("Invoice not found with id: " + id);
    }
    invoiceRepository.deleteById(id);
  }
}