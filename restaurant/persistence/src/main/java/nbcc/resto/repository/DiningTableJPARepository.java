package nbcc.resto.repository;

import nbcc.resto.entity.DiningTableEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiningTableJPARepository extends JpaRepository<DiningTableEntity,Long> {
    boolean existsByNameIgnoreCase(String name);
    List<DiningTableEntity> findByArchivedFalse();
}
