package dashboard.repository;

import dashboard.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Member repository
 */
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findAllByOrderByName();
}
