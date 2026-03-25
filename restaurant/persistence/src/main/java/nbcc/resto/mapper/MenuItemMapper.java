package nbcc.resto.mapper;

import nbcc.resto.dto.MenuItem;
import nbcc.resto.entity.MenuItemEntity;
import org.springframework.stereotype.Component;

@Component
public class MenuItemMapper implements EntityMapper<MenuItem, MenuItemEntity> {

    private final MenuMapper menuMapper;

    public MenuItemMapper(MenuMapper menuMapper) {
        this.menuMapper = menuMapper;
    }


    @Override
    public MenuItem toDTO(MenuItemEntity entity) {
        if(entity == null){
            return null;
        }

        return new MenuItem()
                .setId(entity.getId())
                .setName(entity.getName())
                .setDescription(entity.getDescription())
                .setVersion(entity.getVersion())
                .setMenu(menuMapper.toDTO(entity.getMenu()));
    }

    @Override
    public MenuItemEntity toEntity(MenuItem dto) {
        if(dto == null){
            return null;
        }

        return new MenuItemEntity()
                .setId(dto.getId())
                .setName(dto.getName())
                .setDescription(dto.getDescription())
                .setVersion(dto.getVersion())
                .setMenu(menuMapper.toEntity(dto.getMenu()));
    }
}
