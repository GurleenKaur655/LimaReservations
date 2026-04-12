package nbcc.resto.mapper;

import nbcc.resto.dto.Seating;
import nbcc.resto.entity.SeatingEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

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
                .setTables(entity.getTables() == null ? new ArrayList<>() :
                        entity.getTables().stream()
                                .map(diningTableMapper::toDTO)
                                .toList())
                .setCreatedDate(entity.getCreatedAt())
                .setVersion(entity.getVersion())
                .setArchived(entity.getArchived());
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
                .setTables(dto.getTables() == null ? new ArrayList<>() :
                        dto.getTables().stream()
                                .map(diningTableMapper::toEntity)
                                .toList())
                .setVersion(dto.getVersion())
                .setArchived(dto.getArchived());
    }
}
