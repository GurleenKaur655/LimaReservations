package nbcc.resto.repository;

import nbcc.resto.dto.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    List<Reservation> getAll();
    Optional<Reservation> get(Long id);
    Reservation create(Reservation reservation);
}
