package nbcc.resto.viewmodel;

import nbcc.resto.dto.Menu;
import nbcc.resto.dto.MenuItem;
import org.jspecify.annotations.NonNull;

import java.util.Collection;
import java.util.Iterator;

public class MenuItemListViewModel implements Iterable<MenuItem>{

    private final Long menuId;

    private final boolean canAdd;
    private final boolean canEdit;
    private final boolean canDelete;

    private final String message;

    private final Collection<MenuItem> menuItems;

    private boolean showMenu;

    public MenuItemListViewModel( Collection<MenuItem> menuItems, Long menuId, boolean canManage,  String message) {
        this.menuItems = menuItems;
        this.canAdd = canManage;
        this.canEdit = canManage;
        this.canDelete = canManage;
        this.message = message;
        this.menuId = menuId;
        this.showMenu = false;
    }


    public MenuItemListViewModel(Collection<MenuItem> menuItems,Long menuId, boolean canManage, boolean showMenu) {
        this(menuItems,menuId, canManage, null);
        this.showMenu = showMenu;
    }

    public Long getMenuId() {
        return menuId;
    }

    public boolean isCanAdd() {
        return canAdd;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public String getMessage() {
        return message;
    }

    public boolean isEmpty() {
        return menuItems.isEmpty();
    }

    public Collection<MenuItem> getMenuItems() {
        return menuItems;
    }

    public boolean isShowMenu() {
        return showMenu;
    }

    @Override
    @NonNull
    public Iterator<MenuItem> iterator() {
        return getMenuItems().iterator();
    }
}
