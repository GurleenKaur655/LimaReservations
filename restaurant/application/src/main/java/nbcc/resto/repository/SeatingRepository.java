package nbcc.resto.repository;


import nbcc.resto.dto.Seating;

import java.util.List;
import java.util.Optional;

public interface SeatingRepository {

    List<Seating> getAll();
    Optional<Seating> get(Long id);
    List<Seating> getByTableId(Long tableId);
    Seating create(Seating seating);
    Boolean exists(String name);
}
