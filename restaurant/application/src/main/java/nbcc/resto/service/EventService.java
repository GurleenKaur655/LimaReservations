package nbcc.resto.service;


import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.resto.dto.Event;

import java.time.LocalDate;
import java.util.Collection;

public interface EventService {
    Result<Collection<Event>> getAll();
    ValidatedResult<Event> get(Long id);
    ValidatedResult<Event> create(Event event);
    ValidatedResult<Event> update(Event event);
    ValidatedResult<Void> delete(Long id);
    ValidatedResult<Void> archive(Long id);

    ValidatedResult<Collection<Event>> search(String name, LocalDate startDate, LocalDate endDate);

}
