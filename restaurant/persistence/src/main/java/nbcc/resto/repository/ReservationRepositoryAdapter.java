package nbcc.resto.repository;

import nbcc.resto.dto.Reservation;
import nbcc.resto.dto.Seating;
import nbcc.resto.mapper.ReservationMapper;
import nbcc.resto.mapper.SeatingMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ReservationRepositoryAdapter implements ReservationRepository {

    private final ReservationMapper reservationMapper;
    private final ReservationJPARepository reservationJPARepository;
    private final EventJPARepository eventJPARepository;
    private final SeatingJPARepository seatingJPARepository;

    public ReservationRepositoryAdapter(ReservationMapper reservationMapper, SeatingJPARepository seatingJPARepository, EventJPARepository eventJPARepository, ReservationJPARepository reservationJPARepository) {
        this.reservationMapper = reservationMapper;
        this.seatingJPARepository = seatingJPARepository;
        this.eventJPARepository = eventJPARepository;
        this.reservationJPARepository = reservationJPARepository;
    }

    @Override
    public List<Reservation> getAll() {
        return List.of();
    }

    @Override
    public Optional<Reservation> get(Long id) {
        return Optional.empty();
    }

    @Override
    public Reservation create(Reservation reservation) {
        var entity = reservationMapper.toEntity(reservation);
        entity = reservationJPARepository.save(entity);
        return reservationMapper.toDTO(entity);
    }
}
