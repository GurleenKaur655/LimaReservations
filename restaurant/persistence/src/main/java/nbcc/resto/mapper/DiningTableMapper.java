package nbcc.resto.mapper;

import nbcc.resto.dto.DiningTable;
import nbcc.resto.entity.DiningTableEntity;
import org.springframework.stereotype.Component;

@Component
public class DiningTableMapper implements EntityMapper<DiningTable, DiningTableEntity>{


    public DiningTableMapper() {
    }

    @Override
    public DiningTable toDTO(DiningTableEntity entity){

        if(entity == null){
            return null;
        }

        return new DiningTable()
                .setId(entity.getId())
                .setName(entity.getName())
                .setCapacity(entity.getCapacity())
                .setVersion(entity.getVersion())
                .setArchived(entity.getArchived());
    }


    @Override
    public DiningTableEntity toEntity(DiningTable dto){

        if(dto == null){
            return null;
        }

        return new DiningTableEntity()
                .setId(dto.getId())
                .setName(dto.getName())
                .setCapacity(dto.getCapacity())
                .setVersion(dto.getVersion())
                .setArchived(dto.getArchived());
    }
}
