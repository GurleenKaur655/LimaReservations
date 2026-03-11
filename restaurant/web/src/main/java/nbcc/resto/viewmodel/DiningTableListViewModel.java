package nbcc.resto.viewmodel;

import nbcc.resto.dto.DiningTable;

import java.util.Collection;

public class DiningTableListViewModel {

    private final boolean canAdd;
    private final boolean canEdit;
    private final boolean canDelete;

    private final String message;

    private final Collection<DiningTable> tables;

    public DiningTableListViewModel(Collection<DiningTable> tables, boolean canManage) {
        this(tables, canManage, null);
    }

    public DiningTableListViewModel(Collection<DiningTable> tables, boolean canManage, String message) {
        this.tables = tables;
        this.canAdd = canManage;
        this.canEdit = canManage;
        this.canDelete = canManage;
        this.message = message;
    }

    public Collection<DiningTable> getTables() {
        return tables;
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

    public boolean isEmpty() {
        return tables.isEmpty();
    }
}
