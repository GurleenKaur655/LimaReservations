package nbcc.resto.repository;

import nbcc.resto.dto.Reservation;
import nbcc.resto.dto.ReservationStatus;
import nbcc.resto.mapper.ReservationMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ReservationRepositoryAdapter implements ReservationRepository {

    private final ReservationMapper reservationMapper;
    private final ReservationJPARepository reservationJPARepository;

    public ReservationRepositoryAdapter(ReservationMapper reservationMapper,ReservationJPARepository reservationJPARepository) {
        this.reservationMapper = reservationMapper;
        this.reservationJPARepository = reservationJPARepository;
    }

    @Override
    public List<Reservation> getAll() {
        var entities = reservationJPARepository.findAll();
        return reservationMapper.toDTO(entities);
    }

    @Override
    public List<Reservation> getAll(String eventName, ReservationStatus status) {
        var entities = reservationJPARepository.findAllFiltered(eventName, status);
        return reservationMapper.toDTO(entities);
    }

    @Override
    public List<Reservation> getApprovedByEventId(Long eventId) {
        var entities = reservationJPARepository.getApprovedByEventId(eventId);
        return reservationMapper.toDTO(entities);
    }

    @Override
    public Optional<Reservation> get(Long id) {
        var entity = reservationJPARepository.findById(id);
        return reservationMapper.toDTO(entity);
    }

    @Override
    public Reservation create(Reservation reservation) {
        var entity = reservationMapper.toEntity(reservation);
        entity = reservationJPARepository.save(entity);
        return reservationMapper.toDTO(entity);
    }
    @Override
    public Boolean existsBySeatingId(Long seatingId) {
        return reservationJPARepository.existsBySeatingId(seatingId);
    }
    @Override
    public Boolean existsBySeatingEventId(Long eventId) {
        return reservationJPARepository.existsBySeatingEventId(eventId);
    }
    @Override
    public Optional<Reservation> getByUuid(UUID uuid) {
        var entity = reservationJPARepository.findByUuid(uuid);
        if (entity.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(reservationMapper.toDTO(entity.get()));
    }
    @Override
    public Reservation update(Reservation reservation) {
        var entity = reservationMapper.toEntity(reservation);
        entity = reservationJPARepository.save(entity);
        return reservationMapper.toDTO(entity);
    }



    @Override
    public Boolean isTableAlreadyApprovedForSeating(Long seatingId, Long tableId, Long excludeReservationId) {
        return reservationJPARepository.isTableAlreadyApprovedForSeating(seatingId, tableId, excludeReservationId);
    }
}
