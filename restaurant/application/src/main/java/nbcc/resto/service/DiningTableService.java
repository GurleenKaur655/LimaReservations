package nbcc.resto.service;

import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.resto.dto.DiningTable;

import java.util.Collection;

public interface DiningTableService {
    Result<Collection<DiningTable>> getAll();

    ValidatedResult<DiningTable> get(Long id);

    ValidatedResult<DiningTable> create(DiningTable table);

    ValidatedResult<DiningTable> update(DiningTable table);

    ValidatedResult<Void> delete(Long id);

    Result<Collection<DiningTable>> getAllActive();
}
