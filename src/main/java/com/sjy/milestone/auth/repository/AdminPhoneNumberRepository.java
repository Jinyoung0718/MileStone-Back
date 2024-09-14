package com.sjy.milestone.auth.repository;

import com.sjy.milestone.auth.entity.AdminPhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminPhoneNumberRepository extends JpaRepository<AdminPhoneNumber, Long> {
    boolean existsByPhoneNumber(String phoneNumber);
}
