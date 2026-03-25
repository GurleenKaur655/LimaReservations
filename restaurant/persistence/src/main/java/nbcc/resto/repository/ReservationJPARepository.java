package nbcc.resto.repository;

import nbcc.resto.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationJPARepository extends JpaRepository<ReservationEntity, Long> {
}
