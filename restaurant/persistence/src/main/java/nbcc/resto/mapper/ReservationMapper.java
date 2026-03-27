package nbcc.resto.mapper;

import nbcc.resto.dto.Reservation;
import nbcc.resto.entity.ReservationEntity;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper implements EntityMapper<Reservation, ReservationEntity>{

    private final SeatingMapper seatingMapper;

    public ReservationMapper(SeatingMapper seatingMapper) {
       this.seatingMapper = seatingMapper;
    }

    @Override
    public Reservation toDTO(ReservationEntity entity) {
        if (entity == null) return null;

        return new Reservation()
                .setId(entity.getId())
                .setUuid(entity.getUuid())
                .setSeating(seatingMapper.toDTO(entity.getSeating()))
                .setFirstName(entity.getFirstName())
                .setLastName(entity.getLastName())
                .setEmail(entity.getEmail())
                .setGroupSize(entity.getGroupSize())
                .setStatus(entity.getStatus())
                .setVersion(entity.getVersion());

    }

    @Override
    public ReservationEntity toEntity(Reservation dto) {
        if (dto == null) return null;

        return new ReservationEntity()
                .setId(dto.getId())
                .setUuid(dto.getUuid())
                .setSeating(seatingMapper.toEntity(dto.getSeating()))
                .setFirstName(dto.getFirstName())
                .setLastName(dto.getLastName())
                .setEmail(dto.getEmail())
                .setGroupSize(dto.getGroupSize())
                .setStatus(dto.getStatus())
                .setVersion(dto.getVersion());
    }
}
