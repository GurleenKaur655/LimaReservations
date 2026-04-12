package nbcc.resto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Seating {

    private Long id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotNull(message = "Start Date and Time cannot be blank")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDateTime;

    @Positive
    private Integer durationMinutes;

    private Event event;

    private List<DiningTable> tables = new ArrayList<>();

    private Long version;
    private LocalDateTime createdDate;
    private Boolean archived = false;

    public Seating() {}

    public int getTotalCapacity() {
        if (tables == null) return 0;
        return tables.stream()
                .mapToInt(t -> t.getCapacity() != null ? t.getCapacity() : 0)
                .sum();
    }

    public String getTableNamesDisplay() {
        if (tables == null || tables.isEmpty()) {
            return "N/A";
        }

        var tableNames = tables.stream()
                .map(DiningTable::getName)
                .filter(name -> name != null && !name.isBlank())
                .collect(Collectors.joining(", "));

        return tableNames.isBlank() ? "N/A" : tableNames;
    }

    private List<Long> tableIds = new ArrayList<>();

    public List<Long> getTableIds() { return tableIds; }
    public Seating setTableIds(List<Long> tableIds) { this.tableIds = tableIds; return this; }

    public Long getId() { return id; }
    public Seating setId(Long id) { this.id = id; return this; }

    public String getName() { return name; }
    public Seating setName(String name) { this.name = name; return this; }

    public LocalDateTime getStartDateTime() { return startDateTime; }
    public Seating setStartDateTime(LocalDateTime startDateTime) { this.startDateTime = startDateTime; return this; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public Seating setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; return this; }

    public Event getEvent() { return event; }
    public Seating setEvent(Event event) { this.event = event; return this; }

    public List<DiningTable> getTables() { return tables; }
    public Seating setTables(List<DiningTable> tables) { this.tables = tables; return this; }

    public Long getVersion() { return version; }
    public Seating setVersion(Long version) { this.version = version; return this; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public Seating setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; return this; }

    public Boolean getArchived() { return archived; }
    public Seating setArchived(Boolean archived) { this.archived = archived; return this; }
}
