package dashboard.repository;

import dashboard.domain.GerritChange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by sojan on 6/10/16.
 */
public interface GerritChangeRepository extends JpaRepository<GerritChange, Long> {
    List<GerritChange> findByStatus(String status);
}
