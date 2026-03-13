package nbcc.resto.mapper;

import nbcc.resto.dto.Seating;
import nbcc.resto.entity.SeatingEntity;
import org.springframework.stereotype.Component;

@Component
public class SeatingMapper implements EntityMapper<Seating, SeatingEntity>{

    private final DiningTableMapper diningTableMapper;
    private final EventMapper eventMapper;

    public SeatingMapper(DiningTableMapper diningTableMapper, EventMapper eventMapper) {
        this.diningTableMapper = diningTableMapper;
        this.eventMapper = eventMapper;
    }

    @Override
    public Seating toDTO(SeatingEntity entity) {

        if(entity == null){
            return null;
        }

        return new  Seating()
                .setId(entity.getId())
                .setName(entity.getName())
                .setStartDateTime(entity.getStartDateTime())
                .setDurationMinutes(entity.getDurationMinutes())
                .setEvent(eventMapper.toDTO(entity.getEvent()))
                .setTable(diningTableMapper.toDTO(entity.getTable()))
                .setCreatedDate(entity.getCreatedAt())
                .setVersion(entity.getVersion());
    }

    @Override
    public SeatingEntity toEntity(Seating dto) {
        if(dto == null){
            return null;
        }

        return new  SeatingEntity()
                .setId(dto.getId())
                .setName(dto.getName())
                .setStartDateTime(dto.getStartDateTime())
                .setDurationMinutes(dto.getDurationMinutes())
                .setEvent(eventMapper.toEntity(dto.getEvent()))
                .setTable(diningTableMapper.toEntity(dto.getTable()))
                .setCreatedAt(dto.getCreatedDate())
                .setVersion(dto.getVersion());
    }
}
