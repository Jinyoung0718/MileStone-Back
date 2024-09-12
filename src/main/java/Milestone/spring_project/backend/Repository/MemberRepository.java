package Milestone.spring_project.backend.Repository;

import Milestone.spring_project.backend.domain.Entity.Auth.Member;
import Milestone.spring_project.backend.domain.Entity.Auth.MemberStatus;
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
}


