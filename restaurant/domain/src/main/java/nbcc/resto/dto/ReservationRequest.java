package nbcc.resto.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReservationRequest {

    @NotNull(message = "Event ID is required")
    private Long eventId;

    @NotNull(message = "Seating ID is required")
    private Long seatingId;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email address")
    private String email;

    @NotNull(message = "Group size is required")
    @Min(value = 1, message = "Group size must be at least 1")
    private Integer groupSize;

    public Long getEventId() {
        return eventId;
    }

    public ReservationRequest setEventId(Long eventId) {
        this.eventId = eventId;
        return this;
    }

    public Long getSeatingId() {
        return seatingId;
    }

    public ReservationRequest setSeatingId(Long seatingId) {
        this.seatingId = seatingId;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public ReservationRequest setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public ReservationRequest setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public ReservationRequest setEmail(String email) {
        this.email = email;
        return this;
    }

    public Integer getGroupSize() {
        return groupSize;
    }

    public ReservationRequest setGroupSize(Integer groupSize) {
        this.groupSize = groupSize;
        return this;
    }
}