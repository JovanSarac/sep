package com.pcc.PCC.repository;

import com.pcc.PCC.model.QRCodeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QRCodeRequestRepository extends JpaRepository<QRCodeRequest, Long> {
}
