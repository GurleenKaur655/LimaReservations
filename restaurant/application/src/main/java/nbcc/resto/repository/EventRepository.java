package nbcc.resto.repository;

import nbcc.common.exception.ConcurrencyException;
import nbcc.resto.dto.Event;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EventRepository {
    List<Event> getAll();

    Optional<Event> get(Long id);

    Event create(Event event);

    Event update(Event event) throws ConcurrencyException;


    void delete(Long id);

    List<Event> search(String name, LocalDate startDate, LocalDate endDate);

    void archive(Long id);
    Boolean exists(String name);
}
