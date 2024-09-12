package Milestone.spring_project.backend.Repository;

import Milestone.spring_project.backend.domain.Entity.Auth.AdminPhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminPhoneNumberRepository extends JpaRepository<AdminPhoneNumber, Long> {
    boolean existsByPhoneNumber(String phoneNumber);
}
