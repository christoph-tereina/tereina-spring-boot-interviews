package com.tereina.repository;

import com.tereina.entity.PaymentRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRequestRepository extends JpaRepository<PaymentRequest, Long> {

  List<PaymentRequest> findByPaymentBatchId(Long paymentBatchId);

  List<PaymentRequest> findByInvoiceReference(String invoiceReference);
}