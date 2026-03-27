package nbcc.resto.repository;

import nbcc.common.exception.ConcurrencyException;
import nbcc.resto.dto.Seating;
import nbcc.resto.mapper.SeatingMapper;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SeatingRepositoryAdapter implements SeatingRepository {

    private final SeatingMapper seatingMapper;
    private final SeatingJPARepository seatingJPARepository;
    private final EventJPARepository eventJPARepository;
    private final DiningTableJPARepository diningTableJPARepository;

    public SeatingRepositoryAdapter(SeatingMapper seatingMapper, SeatingJPARepository seatingJPARepository, EventJPARepository eventJPARepository, DiningTableJPARepository diningTableJPARepository) {
        this.seatingMapper = seatingMapper;
        this.seatingJPARepository = seatingJPARepository;
        this.eventJPARepository = eventJPARepository;
        this.diningTableJPARepository = diningTableJPARepository;
    }

    @Override
    public List<Seating> getAll() {
        var entities = seatingJPARepository.findByArchivedFalse();
        return seatingMapper.toDTO(entities);
    }

    @Override
    public Optional<Seating> get(Long id) {
        var entity = seatingJPARepository.findById(id);
        return seatingMapper.toDTO(entity);
    }

    @Override
    public List<Seating> getByTableId(Long tableId) {
        var entities = seatingJPARepository.findByTableId(tableId);
        return seatingMapper.toDTO(entities);
    }
    @Override
    public List<Seating> getAll(Long eventId) {
        var entities = seatingJPARepository.findAllByEventId(eventId);
        return seatingMapper.toDTO(entities);
    }

    @Override
    public Seating create(Seating seating) {
        var entity = seatingMapper.toEntity(seating);
        var event = eventJPARepository.findById(seating.getEvent().getId())
                .orElseThrow();

        var table = diningTableJPARepository.findById(seating.getTable().getId())
                .orElseThrow();

        entity.setEvent(event);
        entity.setTable(table);

        entity = seatingJPARepository.save(entity);
        return seatingMapper.toDTO(entity);
    }

    @Override
    public Seating update(Seating seating) throws ConcurrencyException {
        try {
            var entity = seatingMapper.toEntity(seating);
            var event = eventJPARepository.findById(seating.getEvent().getId()).orElseThrow();
            var table = diningTableJPARepository.findById(seating.getTable().getId()).orElseThrow();
            entity.setEvent(event);
            entity.setTable(table);
            entity = seatingJPARepository.save(entity);
            return seatingMapper.toDTO(entity);
        } catch (ConcurrencyFailureException e) {
            throw new ConcurrencyException(e);
        }
    }

    @Override
    public void delete(Long id) {
        seatingJPARepository.deleteById(id);
    }

    @Override
    public void archive(Long id) {
        seatingJPARepository.findById(id).ifPresent(seatingEntity -> {
            seatingEntity.setArchived(true);
            seatingJPARepository.save(seatingEntity);
        });
    }

    @Override
    public Boolean exists(String name) {
        return seatingJPARepository.existsByNameIgnoreCase(name);
    }
}
