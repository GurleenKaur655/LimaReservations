package nbcc.resto.viewmodel;

import nbcc.resto.dto.DiningTable;
import nbcc.resto.dto.Menu;

import java.util.Collection;

public class MenuViewModel {

    private final boolean canAdd;
    private final boolean canEdit;
    private final boolean canDelete;

    private final String message;

    private final Collection<Menu> menus;

    public MenuViewModel( Collection<Menu> menus,  boolean canManage,  String message) {
        this.menus = menus;
        this.canAdd = canManage;
        this.canEdit = canManage;
        this.canDelete = canManage;
        this.message = message;
    }

    public MenuViewModel(Collection<Menu> menus, boolean canManage) {
        this(menus, canManage, null);
    }

    public boolean isCanAdd() {
        return canAdd;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public String getMessage() {
        return message;
    }

    public Collection<Menu> getMenus() {
        return menus;
    }

    public boolean isEmpty() {
        return  menus.isEmpty();
    }
}
