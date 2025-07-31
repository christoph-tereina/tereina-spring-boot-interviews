package com.tereina.repository;

import com.tereina.entity.PaymentBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentBatchRepository extends JpaRepository<PaymentBatch, Long> {

  Optional<PaymentBatch> findByBatchReference(String batchReference);

  boolean existsByBatchReference(String batchReference);

  @Query("SELECT pb FROM PaymentBatch pb LEFT JOIN FETCH pb.paymentRequests WHERE pb.id = :id")
  Optional<PaymentBatch> findByIdWithPaymentRequests(Long id);
}