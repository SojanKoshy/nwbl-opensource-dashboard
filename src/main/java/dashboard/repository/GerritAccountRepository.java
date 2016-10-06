package dashboard.repository;

import dashboard.domain.GerritAccount;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by sojan on 6/10/16.
 */
public interface GerritAccountRepository extends JpaRepository<GerritAccount, Long> {
}
