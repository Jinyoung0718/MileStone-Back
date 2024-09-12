package Milestone.spring_project.backend.Repository;

import Milestone.spring_project.backend.domain.Entity.Auth.MemberAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberAddressRepository extends JpaRepository<MemberAddress, Long> {

     @Query("SELECT ma FROM MemberAddress ma JOIN FETCH ma.member m WHERE ma.member.userEmail = :userEmail AND ma.isDefault = true")
     Optional<MemberAddress> findByMemberUserEmailAndIsDefaultTrue(@Param("userEmail") String userEmail);


     @Query("SELECT ma FROM MemberAddress ma WHERE ma.member.userEmail = :userEmail AND ma.isDefault = false")
     List<MemberAddress> findByMemberUserEmailAndIsDefaultFalse(@Param("userEmail") String userEmail);
}