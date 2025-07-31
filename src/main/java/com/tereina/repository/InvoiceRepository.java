package com.tereina.repository;

import com.tereina.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

  Optional<Invoice> findByInvoiceReference(String invoiceReference);

  boolean existsByInvoiceReference(String invoiceReference);
}