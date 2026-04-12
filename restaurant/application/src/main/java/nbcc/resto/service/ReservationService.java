package nbcc.resto.service;

import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.resto.dto.Event;
import nbcc.resto.dto.Reservation;
import nbcc.resto.dto.ReservationStatus;

import java.util.Collection;
import java.util.UUID;

public interface ReservationService {
    Result<Collection<Reservation>> getAll();
    ValidatedResult<Reservation> get(Long id);
    ValidatedResult<Reservation> create(Reservation reservation);
    Boolean existsBySeatingId(Long seatingId);
    Boolean existsBySeatingEventId(Long eventId);
    ValidatedResult<Reservation> getByUuid(UUID uuid);
    Result<Collection<Reservation>> getAll(String eventName, ReservationStatus status);
    ValidatedResult<Reservation> approve(Long id, Long tableId);
    ValidatedResult<Reservation> deny(Long id);
    Result<Collection<Reservation>> getApprovedByEventId(Long eventId);
}
