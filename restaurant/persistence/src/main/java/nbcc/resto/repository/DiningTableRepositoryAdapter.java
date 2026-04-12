package nbcc.resto.repository;

import nbcc.common.exception.ConcurrencyException;
import nbcc.resto.dto.DiningTable;
import nbcc.resto.mapper.DiningTableMapper;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DiningTableRepositoryAdapter implements DiningTableRepository{

    private final DiningTableMapper diningTableMapper;
    private final DiningTableJPARepository  diningTableJPARepository;

    public DiningTableRepositoryAdapter(DiningTableMapper diningTableMapper, DiningTableJPARepository diningTableJPARepository) {
        this.diningTableMapper = diningTableMapper;
        this.diningTableJPARepository = diningTableJPARepository;
    }

    @Override
    public List<DiningTable> getAll() {
        var entities = diningTableJPARepository.findAll();
        return diningTableMapper.toDTO(entities);
    }

    @Override
    public Optional<DiningTable> get(Long id) {
        var entity = diningTableJPARepository.findById(id);
        return diningTableMapper.toDTO(entity);
    }


    @Override
    public DiningTable create(DiningTable table) {
        var entity = diningTableMapper.toEntity(table);
        entity = diningTableJPARepository.save(entity);
        return diningTableMapper.toDTO(entity);
    }

    @Override
    public DiningTable update(DiningTable table) throws ConcurrencyException {
        try {
            var entity = diningTableMapper.toEntity(table);
            entity = diningTableJPARepository.save(entity);
            return diningTableMapper.toDTO(entity);
        } catch (ConcurrencyFailureException e) {
            throw new ConcurrencyException(e);
        }
    }

    @Override
    public void delete(Long id) {
        diningTableJPARepository.deleteById(id);
    }

    @Override
    public List<DiningTable> getAllActive() {
        var entities = diningTableJPARepository.findByArchivedFalse();
        return diningTableMapper.toDTO(entities);
    }

    @Override
    public Boolean exists(String name) {
        return diningTableJPARepository.existsByNameIgnoreCase(name);
    }
}
