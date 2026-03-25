package nbcc.resto.viewmodel;
import nbcc.resto.dto.Seating;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SeatingViewModel {

    private final boolean canAdd;
    private final String message;
    private final boolean canEdit;
    private final boolean canDelete;

    private final Collection<Seating> seatings;
    private final Map<String, List<Seating>> seatingsByEventName;

    public SeatingViewModel(Collection<Seating> seatings, boolean canManage) {
        this(seatings, canManage, null);
    }

    public SeatingViewModel(Collection<Seating> seatings, boolean canManage, String message) {

        this.seatings = seatings.stream()
                .sorted((a, b) -> a.getEvent().getName().compareToIgnoreCase(b.getEvent().getName()))
                .toList();
        this.canAdd = canManage;
        this.message = message;
        this.canEdit = canManage;
        this.canDelete = canManage;


        this.seatingsByEventName = this.seatings.stream()
                .collect(Collectors.groupingBy(
                        s -> s.getEvent().getName(),
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
    }

    public Collection<Seating> getSeatings() {
        return seatings;
    }

    public boolean isCanAdd() {
        return canAdd;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public boolean isCanDelete() { return canDelete; }

    public String getMessage() {
        return message;
    }

    public boolean isEmpty() {
        return seatings.isEmpty();
    }

    public Map<String, List<Seating>> getSeatingsByEventName() {
        return seatingsByEventName;
    }
}