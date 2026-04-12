package nbcc.resto.repository;

import nbcc.resto.entity.EventEntity;
import nbcc.resto.entity.SeatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatingJPARepository extends JpaRepository<SeatingEntity, Long> {
    boolean existsByNameIgnoreCase(String name);
    List<SeatingEntity> findByTableId(Long tableId);
    List<SeatingEntity> findAllByEventId(Long eventId);
    List<SeatingEntity> findByArchivedFalse();
    void deleteByTableId(Long tableId);
}
