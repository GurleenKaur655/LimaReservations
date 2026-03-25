package nbcc.resto.service;

import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.resto.dto.Event;
import nbcc.resto.dto.Reservation;

import java.util.Collection;

public interface ReservationService {
//      +getAll() Result~List~Reservation~~
//            +getAll(Long eventId) Result~List~Reservation~~
//            +getAll(ReservationStatus status) Result~List~Reservation~~
//            +getByUUID(UUID uuid) Result~ReservationRequest~
//            +create(Reservation reservation) Result~Reservation~
//            +approve(UUID uuid, Long diningTableId) Result~Reservation~
//            +update(Reservation reservation) Result~Reservation~
//            +deny(UUID uuid) Result~Reservation~

    Result<Collection<Reservation>> getAll();
    ValidatedResult<Reservation> get(Long id);
    ValidatedResult<Reservation> create(Reservation reservation);
}
