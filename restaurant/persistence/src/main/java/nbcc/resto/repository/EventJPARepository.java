package nbcc.resto.repository;

import nbcc.resto.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventJPARepository extends JpaRepository<EventEntity, Long> {
}
