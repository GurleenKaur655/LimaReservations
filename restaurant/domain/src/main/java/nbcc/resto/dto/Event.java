package nbcc.resto.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Event {

    private Long id;


    private String name;


    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private Integer durationMinutes;

    private BigDecimal price;

    private Boolean active;

    private Boolean archived;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Long version;

    public Event() {

    }

    public Event(Event event) {
        this(event.getId(), event.getName(), event.getDescription(), event.getStartDate(), event.getEndDate(),
                event.getDurationMinutes(), event.getPrice(), event.getActive(), event.getArchived(),
                 event.getCreatedAt(), event.getUpdatedAt(), event.getVersion());
    } //this calls another constructor in this same class with these exact parameters

    public Event(Long id, String name, String description,
                 LocalDate startDate, LocalDate endDate,
                 Integer durationMinutes, BigDecimal price,
                 Boolean active, Boolean archived,
                 LocalDateTime createdAt, LocalDateTime updatedAt,
                 Long version) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.durationMinutes = durationMinutes;
        this.price = price;
        this.active = active;
        this.archived = archived;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public Event setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Event setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Event setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public Event setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public Event setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
        return this;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Event setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Event setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public Boolean getActive() {
        return active;
    }

    public Event setActive(Boolean active) {
        this.active = active;
        return this;
    }

    public Boolean getArchived() {
        return archived;
    }

    public Event setArchived(Boolean archived) {
        this.archived = archived;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Event setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Event setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    public Event setVersion(Long version) {
        this.version = version;
        return this;
    }
}
