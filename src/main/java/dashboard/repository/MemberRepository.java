package dashboard.repository;

import dashboard.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by sojan on 6/10/16.
 */
public interface MemberRepository extends JpaRepository<Member, Long> {
    public List<Member> findAllByOrderByName();
}
