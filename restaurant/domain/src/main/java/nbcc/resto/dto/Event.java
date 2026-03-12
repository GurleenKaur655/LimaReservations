package nbcc.resto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Event {

    private Long id;

    @NotBlank
    private String name;

    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotNull
    private Integer durationMinutes;

    @NotNull
    private BigDecimal price;

    private Boolean active;


    private Boolean archived = false;

    private Long version;

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public Event setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public LocalDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public Event setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
        return this;
    }

    private LocalDateTime createdDate;

    private LocalDateTime lastUpdatedDate;

    public Event() {

    }

    public Event(Event event) {
        this(event.getId(), event.getName(), event.getDescription(), event.getStartDate(), event.getEndDate(),
                event.getDurationMinutes(), event.getPrice(), event.getActive(), event.getArchived(),
                event.getVersion(), event.getCreatedDate(), event.getLastUpdatedDate());
    } //this calls another constructor in this same class with these exact parameters

    public Event(Long id, String name, String description,
                 LocalDate startDate, LocalDate endDate,
                 Integer durationMinutes, BigDecimal price,
                 Boolean active, Boolean archived,
                 Long version, LocalDateTime createdDate, LocalDateTime lastUpdatedDate) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.durationMinutes = durationMinutes;
        this.price = price;
        this.active = active;
        this.archived = archived;
        this.version = version;
        this.createdDate = createdDate;
        this.lastUpdatedDate = lastUpdatedDate;
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


    public Long getVersion() {
        return version;
    }

    public Event setVersion(Long version) {
        this.version = version;
        return this;
    }

    public boolean isArchived(){
        return Boolean.TRUE.equals(archived);
    }
}
