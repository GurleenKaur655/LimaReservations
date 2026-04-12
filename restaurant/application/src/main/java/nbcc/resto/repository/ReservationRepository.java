package nbcc.resto.repository;

import nbcc.resto.dto.Reservation;
import nbcc.resto.dto.ReservationStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository {
    List<Reservation> getAll();
    List<Reservation> getAll(String eventName, ReservationStatus status);
    List<Reservation> getApprovedByEventId(Long eventId);
    Optional<Reservation> get(Long id);
    Reservation create(Reservation reservation);
    Optional<Reservation> getByUuid(UUID uuid);
    Boolean existsBySeatingId(Long seatingId);
    Boolean existsBySeatingEventId(Long eventId);
    Reservation update(Reservation reservation);
    Boolean isSeatingAlreadyApproved(Long seatingId, Long excludeReservationId);
    void deleteBySeatingId(Long seatingId);
}
