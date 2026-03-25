package nbcc.resto.mapper;

import nbcc.resto.dto.Menu;
import nbcc.resto.entity.MenuEntity;
import org.springframework.stereotype.Component;

@Component
public class MenuMapper implements EntityMapper<Menu, MenuEntity>{

    public MenuMapper() {
    }

    @Override
    public Menu toDTO(MenuEntity entity) {
        if(entity == null){
            return null;
        }

        return new Menu()
                .setId(entity.getId())
                .setName(entity.getName())
                .setDescription(entity.getDescription())
                .setVersion(entity.getVersion());
    }

    @Override
    public MenuEntity toEntity(Menu dto) {

        if (dto == null) {
            return null;
        }

        return new MenuEntity()
                .setId(dto.getId())
                .setName(dto.getName())
                .setDescription(dto.getDescription())
                .setVersion(dto.getVersion());
    }
}
