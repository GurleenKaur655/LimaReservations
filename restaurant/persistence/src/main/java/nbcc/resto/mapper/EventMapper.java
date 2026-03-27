package nbcc.resto.mapper;

import nbcc.resto.dto.Event;
import nbcc.resto.entity.EventEntity;
import org.springframework.stereotype.Component;

@Component
public class EventMapper implements EntityMapper<Event, EventEntity> {

    private final MenuMapper menuMapper;

    public EventMapper(MenuMapper menuMapper) {
        this.menuMapper = menuMapper;
    }


    public Event toDTO(EventEntity entity){

        if(entity == null){
            return null;
        }

        return new Event()
                .setId(entity.getId())
                .setName(entity.getName())
                .setDescription(entity.getDescription())
                .setStartDate(entity.getStartDate())
                .setEndDate(entity.getEndDate())
                .setPrice(entity.getPrice())
                .setDurationMinutes(entity.getDurationMinutes())
                .setActive(entity.getActive())
                .setArchived(entity.getArchived())
                .setMenu(menuMapper.toDTO(entity.getMenu()))
                .setVersion(entity.getVersion())
                .setCreatedDate(entity.getCreatedAt())
                .setLastUpdatedDate(entity.getUpdatedAt());

    }


    public EventEntity toEntity(Event dto){

        if(dto == null){
            return null;
        }

        return new EventEntity()
                .setId(dto.getId())
                .setName(dto.getName())
                .setDescription(dto.getDescription())
                .setStartDate(dto.getStartDate())
                .setEndDate(dto.getEndDate())
                .setPrice(dto.getPrice())
                .setDurationMinutes(dto.getDurationMinutes())
                .setActive(dto.getActive())
                .setArchived(dto.getArchived())
                .setMenu(menuMapper.toEntity(dto.getMenu()))
                .setVersion(dto.getVersion())
                .setCreatedAt(dto.getCreatedDate())
                .setUpdatedAt(dto.getLastUpdatedDate());
    }
}
