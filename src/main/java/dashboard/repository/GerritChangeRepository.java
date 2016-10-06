package dashboard.repository;

import dashboard.domain.GerritChange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Gerrit change repository
 */
public interface GerritChangeRepository extends JpaRepository<GerritChange, Long> {
    List<GerritChange> findByStatus(String status);
}
