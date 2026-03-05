package nbcc.resto.repository;

import nbcc.common.exception.ConcurrencyException;
import nbcc.resto.dto.Event;
import nbcc.resto.mapper.EventMapper;
import org.springframework.dao.ConcurrencyFailureException;

import java.util.List;
import java.util.Optional;

public class EventRepositoryAdapter implements EventRepository {
    private final EventMapper eventMapper;
    private final EventJPARepository eventJPARepository;

    public EventRepositoryAdapter(EventMapper eventMapper, EventJPARepository eventJPARepository) {
        this.eventMapper = eventMapper;
        this.eventJPARepository = eventJPARepository;
    }

    @Override
    public List<Event> getAll() {
        var entities = eventJPARepository.findAll();
        return eventMapper.toDTO(entities);
    }

    @Override
    public Optional<Event> get(Long id) {
        var entity = eventJPARepository.findById(id);
        return eventMapper.toDTO(entity);
    }

    @Override
    public Event create(Event event) {
        var entity = eventMapper.toEntity(event);
        entity = eventJPARepository.save(entity);
        return eventMapper.toDTO(entity);
    }

    @Override
    public Event update(Event event) throws ConcurrencyException {
        try {
            var entity = eventMapper.toEntity(event);
            entity = eventJPARepository.save(entity);
            return eventMapper.toDTO(entity);
        } catch (ConcurrencyFailureException e) {
            throw new ConcurrencyException(e);
        }
    }

    @Override
    public void delete(Long id) {
        eventJPARepository.deleteById(id);
    }

}
