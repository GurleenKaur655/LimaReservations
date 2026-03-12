package nbcc.resto.repository;

import nbcc.resto.dto.Event;
import nbcc.resto.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EventJPARepository extends JpaRepository<EventEntity, Long> {
   boolean existsByNameIgnoreCase(String name);
    List<EventEntity> findByArchivedFalse();
    @Query("""
SELECT e FROM EventEntity e
WHERE e.archived = false
AND (:name IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%')))
AND (:startDate IS NULL OR e.startDate >= :startDate)
AND (:endDate IS NULL OR e.endDate <= :endDate)
""")
    List<EventEntity> search(
            @Param("name") String name,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
