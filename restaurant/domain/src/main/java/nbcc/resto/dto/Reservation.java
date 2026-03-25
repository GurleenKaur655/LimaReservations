package nbcc.resto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Reservation {

    private Long id;

    private Seating seating;

    @NotBlank(message = "First Name is required")
    private String firstName;

    @NotBlank(message = "Last Name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    private String email;

    @NotNull(message = "Group size is required")
    private Integer groupSize;

    private ReservationStatus status = ReservationStatus.PENDING;

    private Long version;

    public Long getId() {
        return id;
    }

    public Reservation setId(Long id) {
        this.id = id;
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
