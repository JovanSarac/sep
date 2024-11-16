package com.example.PSP.repositories;

import com.example.PSP.models.PSPService;
import com.example.PSP.models.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Query("SELECT s FROM Subscription s WHERE s.user.id = :userId")
    List<Subscription> findSubscriptionsByUserId(@Param("userId") Long userId);
}
