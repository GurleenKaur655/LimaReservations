package nbcc.resto.repository;

import nbcc.resto.entity.SeatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface SeatingJPARepository extends JpaRepository<SeatingEntity, Long> {
    boolean existsByNameIgnoreCase(String name);
    List<SeatingEntity> findAllByEventId(Long eventId);
    List<SeatingEntity> findByArchivedFalse();

    Collection<SeatingEntity> findByTablesId(Long tableId);

    void deleteByTablesId(Long tableId);
}
