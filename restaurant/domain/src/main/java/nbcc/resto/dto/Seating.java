package nbcc.resto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;


import java.time.LocalDateTime;

public class Seating {

    private Long id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotNull(message = "Start Date and Time cannot be blank")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDateTime;


    @Positive
    private Integer durationMinutes;

    public Boolean getArchived() {
        return archived;
    }

    public Seating setArchived(Boolean archived) {
        this.archived = archived;
        return this;
    }

    private Event event;

    private DiningTable table;

    private Long version;

    private LocalDateTime createdDate;
    private Boolean archived = false;



    public Seating() {
    }

    public Seating(Seating seating) {
        this(seating.getId(), seating.getName(), seating.getStartDateTime(),
                seating.getDurationMinutes(), seating.getTable(), seating.getEvent(),
                seating.getVersion());
    }

    public Seating(Event event) {
        this.event = event;
    }

    public Seating(DiningTable table) {
        this.table = table;
    }


    public Seating(Long id, String name, LocalDateTime startDateTime, Integer durationMinutes, DiningTable table, Event event, Long version) {
        this.id = id;
        this.name = name;
        this.startDateTime = startDateTime;
        this.durationMinutes = durationMinutes;
        this.table = table;
        this.event = event;
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public Seating setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Seating setName(String name) {
        this.name = name;
        return this;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public Seating setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
        return this;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public Seating setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
        return this;
    }

    public Event getEvent() {
        return event;
    }

    public Seating setEvent(Event event) {
        this.event = event;
        return this;
    }

    public DiningTable getTable() {
        return table;
    }

    public Seating setTable(DiningTable table) {
        this.table = table;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    public Seating setVersion(Long version) {
        this.version = version;
        return this;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public Seating setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }
}
