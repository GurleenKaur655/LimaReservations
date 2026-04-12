package nbcc.resto.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class Reservation {

    private Long id;

    @JsonIgnoreProperties({"event", "table", "version", "createdDate",
            "archived", "durationMinutes", "startDateTime", "tableIds", "tables", "totalCapacity", "tableNamesDisplay"})
    private Seating seating;
    private UUID uuid;

    @NotBlank(message = "First Name is required")
    private String firstName;

    @NotBlank(message = "Last Name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    private String email;

    @NotNull(message = "Group size is required")
    private Integer groupSize;

    private ReservationStatus status = ReservationStatus.PENDING;

    @JsonIgnore
    private Long version;

    @JsonIgnore
    private Long eventId;

    private Long tableId;

    public Long getTableId() { return tableId; }
    public Reservation setTableId(Long tableId) { this.tableId = tableId; return this; }


    public Long getId() {
        return id;
    }

    public Long getEventId() {
        return eventId;
    }

    public Reservation setEventId(Long eventId) {
        this.eventId = eventId;
        return this;
    }

    public Reservation setId(Long id) {
        this.id = id;
        return this;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Reservation setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public Seating getSeating() {
        return seating;
    }

    public Reservation setSeating(Seating seating) {
        this.seating = seating;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public Reservation setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public Reservation setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Reservation setEmail(String email) {
        this.email = email;
        return this;
    }

    public Integer getGroupSize() {
        return groupSize;
    }

    public Reservation setGroupSize(Integer groupSize) {
        this.groupSize = groupSize;
        return this;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public Reservation setStatus(ReservationStatus status) {
        this.status = status;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    public Reservation setVersion(Long version) {
        this.version = version;
        return this;
    }

}
