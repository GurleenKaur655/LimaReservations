package nbcc.resto.repository;

import nbcc.resto.dto.Seating;
import nbcc.resto.mapper.SeatingMapper;

import java.util.List;
import java.util.Optional;

public class SeatingRepositoryAdapter implements SeatingRepository {

    private final SeatingMapper seatingMapper;
    private final SeatingJPARepository seatingJPARepository;

    public SeatingRepositoryAdapter(SeatingMapper seatingMapper, SeatingJPARepository seatingJPARepository) {
        this.seatingMapper = seatingMapper;
        this.seatingJPARepository = seatingJPARepository;
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
        entity = seatingJPARepository.save(entity);
        return seatingMapper.toDTO(entity);
    }

    @Override
    public Boolean exists(String name) {
        return seatingJPARepository.existsByNameIgnoreCase(name);
    }
}
