package nbcc.resto.service;

import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.resto.dto.Event;
import nbcc.resto.dto.Menu;

import java.util.Collection;

public interface MenuService {

    Result<Collection<Menu>> getAll();

    ValidatedResult<Menu> get(Long id);

    ValidatedResult<Menu> getWithMenuItems(Long id);

    ValidatedResult<Menu> create(Menu menu);

    ValidatedResult<Menu> update(Menu menu);

    ValidatedResult<Void> delete(Long id);

    ValidatedResult<Collection<Menu>> search(String name);
}
