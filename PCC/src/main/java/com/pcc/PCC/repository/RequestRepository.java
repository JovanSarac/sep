package com.pcc.PCC.repository;

import com.pcc.PCC.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    Optional<Request> findByAcquirerOrderId(UUID id);
}
