package nbcc.resto.viewmodel;

import nbcc.resto.dto.Event;
import nbcc.resto.dto.Reservation;
import nbcc.resto.dto.ReservationStatus;

import java.util.Collection;

public class ReservationViewModel {

    private final Collection<Reservation> reservations;
    private final Collection<Event> events;
    private final ReservationStatus selectedStatus;

    private final String selectedEventName;

    public ReservationViewModel(Collection<Reservation> reservations,
                                Collection<Event> events,
                                ReservationStatus selectedStatus,
                                String selectedEventName) {
        this.reservations = reservations;
        this.events = events;
        this.selectedStatus = selectedStatus;
        this.selectedEventName = selectedEventName;
    }

    public String getSelectedEventName() { return selectedEventName; }

    public Collection<Reservation> getReservations() { return reservations; }
    public Collection<Event> getEvents() { return events; }
    public ReservationStatus getSelectedStatus() { return selectedStatus; }

    public boolean isEmpty() {
        return reservations == null || reservations.isEmpty();
    }
}