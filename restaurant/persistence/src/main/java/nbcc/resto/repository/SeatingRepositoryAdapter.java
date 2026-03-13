package nbcc.resto.repository;

import nbcc.resto.dto.Seating;
import nbcc.resto.mapper.SeatingMapper;
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
        var entities = seatingJPARepository.findAll();
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
    public Boolean exists(String name) {
        return seatingJPARepository.existsByNameIgnoreCase(name);
    }
}
