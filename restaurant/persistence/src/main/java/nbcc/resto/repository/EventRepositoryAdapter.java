package nbcc.resto.repository;

import jakarta.transaction.Transactional;
import nbcc.common.exception.ConcurrencyException;
import nbcc.resto.dto.Event;
import nbcc.resto.mapper.EventMapper;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class EventRepositoryAdapter implements EventRepository {
    private final EventMapper eventMapper;
    private final EventJPARepository eventJPARepository;

    public EventRepositoryAdapter(EventMapper eventMapper, EventJPARepository eventJPARepository) {
        this.eventMapper = eventMapper;
        this.eventJPARepository = eventJPARepository;
    }

    @Override
    public List<Event> getAll() {
        var entities = eventJPARepository.findByArchivedFalse();
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

    @Override
    public List<Event> search(String name, LocalDate startDate, LocalDate endDate) {
        var events = eventJPARepository.search(name, startDate, endDate);
        return eventMapper.toDTO(events);
    }

    @Override
    @Transactional
    public void archive(Long id) {
        eventJPARepository.findById(id).ifPresent(eventEntity -> {
            eventEntity.setArchived(true);
            eventEntity.setActive(false);
            eventJPARepository.save(eventEntity);
        });
    }

    @Override
    public Boolean exists(String name) {
        return eventJPARepository.existsByNameIgnoreCase(name);
    }

}
