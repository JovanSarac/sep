package com.example.bank1.bank1.repository;

import com.example.bank1.bank1.model.QRPaymentRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QRPaymentRequestRepository extends JpaRepository<QRPaymentRequest, Long> {
    Optional<QRPaymentRequest> findByPaymentId(UUID paymentId);
}
