package nbcc.resto.repository;

import nbcc.resto.dto.ReservationStatus;
import nbcc.resto.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationJPARepository extends JpaRepository<ReservationEntity, Long> {
    boolean existsBySeatingId(Long seatingId);
    boolean existsBySeatingEventId(Long eventId);
    Optional<ReservationEntity> findByUuid(UUID uuid);
    void deleteBySeatingId(Long seatingId);

    @Query("SELECT r FROM ReservationEntity r WHERE " +
            "(:eventName IS NULL OR r.seating.event.name = :eventName) AND " +
            "(:status IS NULL OR r.status = :status) " +
            "ORDER BY r.seating.startDateTime ASC")
    List<ReservationEntity> findAllFiltered(@Param("eventName") String eventName,
                                            @Param("status") ReservationStatus status);

    @Query("SELECT COUNT(r) > 0 FROM ReservationEntity r WHERE " + "r.seating.id = :seatingId AND " + "r.tableId = :tableId AND " + "r.status = 'APPROVED' AND " + "r.id != :excludeId")
    boolean isTableAlreadyApprovedForSeating(@Param("seatingId") Long seatingId, @Param("tableId") Long tableId, @Param("excludeId") Long excludeId);

    @Query("SELECT r FROM ReservationEntity r WHERE " +
            "r.seating.event.id = :eventId AND r.status = 'APPROVED' " +
            "ORDER BY r.seating.startDateTime ASC")
    List<ReservationEntity> getApprovedByEventId(@Param("eventId") Long eventId);
}
