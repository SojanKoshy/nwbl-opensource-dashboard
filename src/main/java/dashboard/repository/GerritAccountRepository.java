package dashboard.repository;

import dashboard.domain.GerritAccount;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Gerrit account repository
 */
public interface GerritAccountRepository extends JpaRepository<GerritAccount, Long> {
}
