package com.sjy.milestone.account.repository;

import com.sjy.milestone.account.entity.AdminPhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminPhoneNumberRepository extends JpaRepository<AdminPhoneNumber, Long> {
    boolean existsByPhoneNumber(String phoneNumber);
}
