package nbcc.resto.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.time.LocalDateTime;

@Entity
@Table(name = "seating")
@EntityListeners(AuditingEntityListener.class)
public class SeatingEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String name;

    @NotNull
    private LocalDateTime startDateTime;

    @NotNull
    private Integer durationMinutes;

    @ManyToOne
    @JoinColumn(name = "event_id", foreignKey = @ForeignKey(name = "fk_seating_event"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private EventEntity event;

    @ManyToOne
    @JoinColumn(name = "table_id", foreignKey = @ForeignKey(name = "fk_seating_table"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private DiningTableEntity table;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Version
    private Long version;

    public SeatingEntity() {
    }

    public Long getId() {
        return id;
    }

    public SeatingEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public @NotNull String getName() {
        return name;
    }

    public SeatingEntity setName(@NotNull String name) {
        this.name = name;
        return this;
    }

    public @NotNull LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public SeatingEntity setStartDateTime(@NotNull LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
        return this;
    }

    public @NotNull Integer getDurationMinutes() {
        return durationMinutes;
    }

    public SeatingEntity setDurationMinutes(@NotNull Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
        return this;
    }

    public EventEntity getEvent() {
        return event;
    }

    public SeatingEntity setEvent(EventEntity event) {
        this.event = event;
        return this;
    }

    public DiningTableEntity getTable() {
        return table;
    }

    public SeatingEntity setTable(DiningTableEntity table) {
        this.table = table;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public SeatingEntity setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    public SeatingEntity setVersion(Long version) {
        this.version = version;
        return this;
    }
}
