package com.sjy.milestone.account.repository;

import com.sjy.milestone.account.entity.Member;
import com.sjy.milestone.account.entity.MemberStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByUserEmail(String userEmail);

    @EntityGraph(attributePaths = {"orders"})
    Member findMemberWithOrdersByUserEmail(String userEmail);

    Optional<Member> findByUserEmailAndStatus(String userEmail, MemberStatus status);

    List<Member> findAllByStatus(MemberStatus memberStatus);

    boolean existsByUserEmail(String email);
}


