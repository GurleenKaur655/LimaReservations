package nbcc.resto.repository;

import nbcc.common.exception.ConcurrencyException;
import nbcc.resto.dto.DiningTable;

import java.util.List;

public interface DiningTableRepository {
    List<DiningTable> getAll();

    DiningTable create(DiningTable table);

    DiningTable update(DiningTable table) throws ConcurrencyException;

    void delete(Long id);
}
