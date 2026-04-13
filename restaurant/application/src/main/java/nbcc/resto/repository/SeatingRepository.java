package nbcc.resto.repository;


import nbcc.common.exception.ConcurrencyException;
import nbcc.resto.dto.Event;
import nbcc.resto.dto.Seating;

import java.util.List;
import java.util.Optional;

public interface SeatingRepository {
    List<Seating> getAll();
    List<Seating> getAll(Long eventId);
    Optional<Seating> get(Long id);
    List<Seating> getByTableId(Long tableId);
    Seating update(Seating seating) throws ConcurrencyException;
    void delete(Long id);
    void archive(Long id);
    Seating create(Seating seating);
    Boolean exists(String name);
}
