package nbcc.resto.viewmodel;

import nbcc.resto.dto.Event;

import java.util.Collection;

public class EventListViewModel {
    private final boolean canAdd;
    private final boolean canEdit;
    private final boolean canDelete;

    private final String message;

    private final Collection<Event> events;



    public EventListViewModel(Collection<Event> events, boolean canManage) {
        this(events, canManage, null);
    }

    public EventListViewModel(Collection<Event> events, boolean canManage, String message) {
        this.events = events;
        this.canAdd = canManage;
        this.canEdit = canManage;
        this.canDelete = canManage;
        this.message = message;
    }

    public Collection<Event> getEvents() {
        return events;
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
        return  events.isEmpty();
    }

}
